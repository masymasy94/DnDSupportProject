package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.domain.model.SourceReference;
import com.dndplatform.documentqa.domain.model.SourceReferenceBuilder;
import com.dndplatform.documentqa.domain.repository.DocumentChunkSearchRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DocumentChunkSearchRepositoryJpa implements DocumentChunkSearchRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Inject
    EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<SourceReference> searchSimilar(float[] queryEmbedding, List<String> documentIds, int topK) {
        log.info(() -> "Searching similar chunks, topK=%d, filter=%d documents".formatted(
                topK, documentIds != null ? documentIds.size() : 0));

        boolean hasFilter = documentIds != null && !documentIds.isEmpty();
        String queryStr = toVectorString(queryEmbedding);

        String sql = """
                SELECT dc.document_id, di.file_name, dc.content,
                       1 - (dc.embedding <=> CAST(:query AS vector)) AS score
                FROM document_chunks dc
                JOIN document_ingestions di ON dc.document_id = di.document_id
                WHERE (:hasFilter = false OR dc.document_id IN (:ids))
                ORDER BY dc.embedding <=> CAST(:query AS vector)
                LIMIT :k
                """;

        Query query = entityManager.createNativeQuery(sql)
                .setParameter("query", queryStr)
                .setParameter("hasFilter", hasFilter)
                .setParameter("ids", hasFilter ? documentIds : List.of(""))
                .setParameter("k", topK);

        List<Object[]> results = query.getResultList();

        List<SourceReference> references = new ArrayList<>();
        for (Object[] row : results) {
            references.add(SourceReferenceBuilder.builder()
                    .withDocumentId((String) row[0])
                    .withFileName((String) row[1])
                    .withExcerpt((String) row[2])
                    .withScore(((Number) row[3]).doubleValue())
                    .build());
        }

        log.info(() -> "Found %d similar chunks".formatted(references.size()));
        return references;
    }

    private String toVectorString(float[] embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(embedding[i]);
        }
        sb.append("]");
        return sb.toString();
    }
}

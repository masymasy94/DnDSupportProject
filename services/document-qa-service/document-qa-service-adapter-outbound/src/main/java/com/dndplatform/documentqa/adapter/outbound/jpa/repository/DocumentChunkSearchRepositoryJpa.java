package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.domain.model.SourceReference;
import com.dndplatform.documentqa.domain.model.SourceReferenceBuilder;
import com.dndplatform.documentqa.domain.repository.DocumentChunkSearchRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DocumentChunkSearchRepositoryJpa implements DocumentChunkSearchRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final DocumentChunkPanacheRepository panacheRepository;

    @Inject
    public DocumentChunkSearchRepositoryJpa(DocumentChunkPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public List<SourceReference> searchSimilar(float[] queryEmbedding, List<String> documentIds, int topK) {
        log.info(() -> "Searching similar chunks, topK=%d, filter=%d documents".formatted(
                topK, documentIds != null ? documentIds.size() : 0));

        boolean hasFilter = documentIds != null && !documentIds.isEmpty();
        String queryStr = toVectorString(queryEmbedding);

        List<Object[]> results = panacheRepository.searchSimilar(queryStr, hasFilter, documentIds, topK);

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

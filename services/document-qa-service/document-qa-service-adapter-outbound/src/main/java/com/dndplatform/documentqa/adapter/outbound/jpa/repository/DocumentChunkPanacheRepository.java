package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.DocumentChunkEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class DocumentChunkPanacheRepository implements PanacheRepository<DocumentChunkEntity> {

    public long deleteByDocumentId(String documentId) {
        return delete("documentId", documentId);
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> searchSimilar(String queryVector, boolean hasFilter, List<String> documentIds, int topK) {
        String sql = """
                SELECT dc.document_id, di.file_name, dc.content,
                       1 - (dc.embedding <=> CAST(:query AS vector)) AS score
                FROM document_chunks dc
                JOIN document_ingestions di ON dc.document_id = di.document_id
                WHERE (:hasFilter = false OR dc.document_id IN (:ids))
                ORDER BY dc.embedding <=> CAST(:query AS vector)
                LIMIT :k
                """;
        return getEntityManager().createNativeQuery(sql)
                .setParameter("query", queryVector)
                .setParameter("hasFilter", hasFilter)
                .setParameter("ids", hasFilter ? documentIds : List.of(""))
                .setParameter("k", topK)
                .getResultList();
    }

    public void updateEmbedding(Long id, String embeddingStr) {
        getEntityManager().createNativeQuery(
                        "UPDATE document_chunks SET embedding = CAST(:emb AS vector) WHERE id = :id")
                .setParameter("emb", embeddingStr)
                .setParameter("id", id)
                .executeUpdate();
    }
}

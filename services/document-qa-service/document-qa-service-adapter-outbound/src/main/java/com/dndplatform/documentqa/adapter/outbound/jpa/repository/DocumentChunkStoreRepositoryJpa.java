package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.DocumentChunkEntity;
import com.dndplatform.documentqa.domain.model.DocumentChunk;
import com.dndplatform.documentqa.domain.repository.DocumentChunkStoreRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DocumentChunkStoreRepositoryJpa implements DocumentChunkStoreRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    public void storeChunks(List<DocumentChunk> chunks, List<float[]> embeddings) {
        log.info(() -> "Storing %d chunks".formatted(chunks.size()));

        for (int i = 0; i < chunks.size(); i++) {
            DocumentChunk chunk = chunks.get(i);
            float[] embedding = embeddings.get(i);

            DocumentChunkEntity entity = new DocumentChunkEntity();
            entity.documentId = chunk.documentId();
            entity.chunkIndex = chunk.chunkIndex();
            entity.content = chunk.content();

            entity.persist();

            String embeddingStr = toVectorString(embedding);
            entityManager.createNativeQuery(
                            "UPDATE document_chunks SET embedding = CAST(:emb AS vector) WHERE id = :id")
                    .setParameter("emb", embeddingStr)
                    .setParameter("id", entity.id)
                    .executeUpdate();
        }

        log.info(() -> "Stored %d chunks successfully".formatted(chunks.size()));
    }

    @Override
    @Transactional
    public void deleteByDocumentId(String documentId) {
        log.info(() -> "Deleting chunks for document: %s".formatted(documentId));

        long deleted = DocumentChunkEntity.delete("documentId", documentId);
        log.info(() -> "Deleted %d chunks for document: %s".formatted(deleted, documentId));
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

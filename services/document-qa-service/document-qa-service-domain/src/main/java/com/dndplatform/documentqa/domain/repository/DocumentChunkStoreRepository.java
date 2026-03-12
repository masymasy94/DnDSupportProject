package com.dndplatform.documentqa.domain.repository;

import com.dndplatform.documentqa.domain.model.DocumentChunk;

import java.util.List;

public interface DocumentChunkStoreRepository {

    void storeChunks(List<DocumentChunk> chunks, List<float[]> embeddings);

    void deleteByDocumentId(String documentId);
}

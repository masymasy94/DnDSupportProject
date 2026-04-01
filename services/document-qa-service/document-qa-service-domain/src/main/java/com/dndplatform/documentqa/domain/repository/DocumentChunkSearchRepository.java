package com.dndplatform.documentqa.domain.repository;

import com.dndplatform.documentqa.domain.model.SourceReference;

import java.util.List;

public interface DocumentChunkSearchRepository {

    List<SourceReference> searchSimilar(float[] queryEmbedding, List<String> documentIds, int topK);
}

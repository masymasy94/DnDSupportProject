package com.dndplatform.documentqa.domain.model;

public enum IngestionStatus {
    PENDING,
    FETCHING_DOCUMENT,
    EXTRACTING_TEXT,
    CHUNKING,
    GENERATING_EMBEDDINGS,
    STORING_VECTORS,
    COMPLETED,
    FAILED
}

package com.dndplatform.documentqa.domain.event;

import com.dndplatform.documentqa.domain.model.IngestionStatus;

public interface DocumentIngestedEventPublisher {

    void publish(String documentId, IngestionStatus status, String errorMessage, Integer chunkCount);
}

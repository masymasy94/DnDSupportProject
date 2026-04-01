package com.dndplatform.documentqa.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record DocumentIngestion(
        String documentId,
        String fileName,
        String contentType,
        IngestionStatus status,
        int chunkCount,
        String errorMessage,
        String uploadedBy,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        LocalDateTime createdAt
) {
}

package com.dndplatform.documentqa.view.model.vm;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record IngestionStatusViewModel(
        String documentId,
        String fileName,
        String status,
        int chunkCount,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        String errorMessage
) {
}

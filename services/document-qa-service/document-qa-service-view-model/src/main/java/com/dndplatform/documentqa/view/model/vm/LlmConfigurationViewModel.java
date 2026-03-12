package com.dndplatform.documentqa.view.model.vm;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record LlmConfigurationViewModel(
        Long id,
        Long userId,
        String name,
        String provider,
        String modelName,
        String baseUrl,
        String embeddingProvider,
        String embeddingModel,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

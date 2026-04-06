package com.dndplatform.documentqa.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record LlmConfiguration(
        Long id,
        Long userId,
        String name,
        LlmProvider provider,
        String modelName,
        String baseUrl,
        String apiKey,
        String embeddingProvider,
        String embeddingModel,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

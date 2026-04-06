package com.dndplatform.documentqa.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.validation.constraints.NotBlank;

@Builder
public record CreateLlmConfigurationRequest(
        Long userId,
        @NotBlank(message = "name is required") String name,
        @NotBlank(message = "provider is required") String provider,
        @NotBlank(message = "modelName is required") String modelName,
        String baseUrl,
        String apiKey,
        String embeddingProvider,
        String embeddingModel
) {
}

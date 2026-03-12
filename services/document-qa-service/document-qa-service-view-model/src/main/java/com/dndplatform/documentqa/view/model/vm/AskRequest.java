package com.dndplatform.documentqa.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Builder
public record AskRequest(
        @NotNull(message = "userId is required") Long userId,
        Long conversationId,
        @NotBlank(message = "question is required") String question,
        List<String> documentIds
) {
}

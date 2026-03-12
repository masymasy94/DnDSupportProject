package com.dndplatform.documentqa.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record QuestionRequest(
        Long userId,
        Long conversationId,
        String question,
        List<String> documentIds
) {
}

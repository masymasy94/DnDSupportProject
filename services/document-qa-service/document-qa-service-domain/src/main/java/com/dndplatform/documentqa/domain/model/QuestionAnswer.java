package com.dndplatform.documentqa.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record QuestionAnswer(
        Long conversationId,
        String answer,
        List<SourceReference> sources
) {
}

package com.dndplatform.documentqa.view.model.vm;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record QuestionAnswerViewModel(
        Long conversationId,
        String answer,
        List<SourceReferenceViewModel> sources
) {
}

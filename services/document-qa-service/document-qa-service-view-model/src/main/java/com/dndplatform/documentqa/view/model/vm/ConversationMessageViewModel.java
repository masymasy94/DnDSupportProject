package com.dndplatform.documentqa.view.model.vm;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record ConversationMessageViewModel(
        Long id,
        String role,
        String content,
        LocalDateTime createdAt
) {
}

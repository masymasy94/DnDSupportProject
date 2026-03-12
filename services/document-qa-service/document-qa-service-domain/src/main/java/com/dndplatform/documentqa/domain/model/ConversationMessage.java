package com.dndplatform.documentqa.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record ConversationMessage(
        Long id,
        Long conversationId,
        String role,
        String content,
        LocalDateTime createdAt
) {
}

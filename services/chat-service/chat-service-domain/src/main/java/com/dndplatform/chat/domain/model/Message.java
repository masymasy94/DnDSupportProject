package com.dndplatform.chat.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record Message(
        Long id,
        Long conversationId,
        Long senderId,
        String content,
        MessageType messageType,
        LocalDateTime createdAt,
        LocalDateTime editedAt,
        LocalDateTime deletedAt
) {}

package com.dndplatform.chat.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record Conversation(
        Long id,
        String name,
        ConversationType type,
        Long createdBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<ConversationParticipant> participants
) {}

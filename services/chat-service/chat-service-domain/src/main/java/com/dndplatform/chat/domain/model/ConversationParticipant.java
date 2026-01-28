package com.dndplatform.chat.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record ConversationParticipant(
        Long id,
        Long conversationId,
        Long userId,
        ParticipantRole role,
        LocalDateTime joinedAt,
        LocalDateTime leftAt,
        LocalDateTime lastReadAt
) {}

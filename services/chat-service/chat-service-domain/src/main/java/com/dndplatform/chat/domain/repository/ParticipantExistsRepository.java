package com.dndplatform.chat.domain.repository;

public interface ParticipantExistsRepository {
    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);
}

package com.dndplatform.chat.domain.repository;

public interface ParticipantUpdateLastReadRepository {
    void updateLastReadAt(Long conversationId, Long userId);
}

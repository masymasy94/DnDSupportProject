package com.dndplatform.chat.domain;

public interface ConversationUpdateReadByIdService {
    void updateReadById(Long conversationId, Long userId);
}

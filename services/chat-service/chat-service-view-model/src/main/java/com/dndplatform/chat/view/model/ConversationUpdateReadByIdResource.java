package com.dndplatform.chat.view.model;

public interface ConversationUpdateReadByIdResource {
    void updateReadById(Long conversationId, Long userId);
}

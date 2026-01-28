package com.dndplatform.chat.domain.repository;

import com.dndplatform.chat.domain.model.Conversation;

import java.util.Optional;

public interface ConversationFindDirectRepository {
    Optional<Conversation> findDirectConversation(Long userId1, Long userId2);
}

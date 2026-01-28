package com.dndplatform.chat.domain.repository;

import com.dndplatform.chat.domain.model.Conversation;

import java.util.Optional;

public interface ConversationFindByIdRepository {
    Optional<Conversation> findById(Long id);
}

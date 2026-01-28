package com.dndplatform.chat.domain;

import com.dndplatform.chat.domain.model.Conversation;

import java.util.Optional;

public interface ConversationFindByIdService {
    Optional<Conversation> findById(Long id, Long userId);
}

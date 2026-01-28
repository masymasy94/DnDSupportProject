package com.dndplatform.chat.domain.repository;

import com.dndplatform.chat.domain.model.Conversation;

import java.util.List;

public interface ConversationFindByUserRepository {
    List<Conversation> findByUserId(Long userId);
}

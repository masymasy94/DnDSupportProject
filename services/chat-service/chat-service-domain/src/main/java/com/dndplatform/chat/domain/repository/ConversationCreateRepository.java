package com.dndplatform.chat.domain.repository;

import com.dndplatform.chat.domain.model.Conversation;

public interface ConversationCreateRepository {
    Conversation create(Conversation conversation);
}

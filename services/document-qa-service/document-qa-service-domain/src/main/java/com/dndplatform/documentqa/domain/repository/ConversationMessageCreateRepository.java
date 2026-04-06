package com.dndplatform.documentqa.domain.repository;

import com.dndplatform.documentqa.domain.model.ConversationMessage;

public interface ConversationMessageCreateRepository {

    ConversationMessage create(Long conversationId, String role, String content);
}

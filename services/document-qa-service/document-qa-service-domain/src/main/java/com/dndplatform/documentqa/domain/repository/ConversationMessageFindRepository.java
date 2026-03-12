package com.dndplatform.documentqa.domain.repository;

import com.dndplatform.documentqa.domain.model.ConversationMessage;

import java.util.List;

public interface ConversationMessageFindRepository {

    List<ConversationMessage> findByConversationId(Long conversationId);
}

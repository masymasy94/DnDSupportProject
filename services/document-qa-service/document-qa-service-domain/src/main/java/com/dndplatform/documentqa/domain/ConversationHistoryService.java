package com.dndplatform.documentqa.domain;

import com.dndplatform.documentqa.domain.model.ConversationMessage;

import java.util.List;

public interface ConversationHistoryService {

    List<ConversationMessage> getHistory(Long conversationId, Long userId);
}

package com.dndplatform.documentqa.domain.repository;

import com.dndplatform.documentqa.domain.model.ConversationMessage;

import java.util.List;

public interface LlmChatRepository {

    String chat(Long userId, String systemPrompt, List<ConversationMessage> history, String context, String question);
}

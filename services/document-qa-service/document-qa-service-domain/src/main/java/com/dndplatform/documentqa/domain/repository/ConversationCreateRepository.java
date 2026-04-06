package com.dndplatform.documentqa.domain.repository;

import com.dndplatform.documentqa.domain.model.Conversation;

public interface ConversationCreateRepository {

    Conversation create(Long userId, Long campaignId, String title);
}

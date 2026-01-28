package com.dndplatform.chat.domain;

import com.dndplatform.chat.domain.model.Conversation;

import java.util.List;

public interface DirectConversationCreateService {
    Conversation create(Long createdBy, List<Long> participantIds);
}

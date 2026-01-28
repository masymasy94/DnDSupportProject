package com.dndplatform.chat.domain;

import com.dndplatform.chat.domain.model.Conversation;

import java.util.List;

public interface GroupConversationCreateService {
    Conversation create(String name, Long createdBy, List<Long> participantIds);
}

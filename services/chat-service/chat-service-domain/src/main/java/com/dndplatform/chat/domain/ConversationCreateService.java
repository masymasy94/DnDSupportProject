package com.dndplatform.chat.domain;

import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.ConversationType;

import java.util.List;

public interface ConversationCreateService {
    Conversation create(ConversationType type, String name, Long createdBy, List<Long> participantIds);
}

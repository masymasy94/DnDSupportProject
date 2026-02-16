package com.dndplatform.chat.domain;

import com.dndplatform.chat.domain.model.Conversation;

import java.util.List;

public interface ConversationFindByUserService {
    List<Conversation> findByUserId(Long userId);
}

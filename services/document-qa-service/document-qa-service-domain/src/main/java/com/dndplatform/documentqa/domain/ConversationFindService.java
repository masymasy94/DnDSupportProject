package com.dndplatform.documentqa.domain;

import com.dndplatform.documentqa.domain.model.Conversation;

import java.util.List;

public interface ConversationFindService {

    List<Conversation> findByUserId(Long userId);

    Conversation findById(Long id, Long userId);
}

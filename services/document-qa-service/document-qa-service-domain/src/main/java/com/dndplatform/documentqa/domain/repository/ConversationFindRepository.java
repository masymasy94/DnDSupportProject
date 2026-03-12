package com.dndplatform.documentqa.domain.repository;

import com.dndplatform.documentqa.domain.model.Conversation;

import java.util.List;
import java.util.Optional;

public interface ConversationFindRepository {

    List<Conversation> findByUserId(Long userId);

    Optional<Conversation> findById(Long id);
}

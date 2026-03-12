package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.documentqa.domain.ConversationFindService;
import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.repository.ConversationFindRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ConversationFindServiceImpl implements ConversationFindService {

    private final ConversationFindRepository repository;

    @Inject
    public ConversationFindServiceImpl(ConversationFindRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Conversation> findByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public Conversation findById(Long id, Long userId) {
        Conversation conversation = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conversation not found: " + id));
        if (!conversation.userId().equals(userId)) {
            throw new ForbiddenException("Access denied to conversation: " + id);
        }
        return conversation;
    }
}

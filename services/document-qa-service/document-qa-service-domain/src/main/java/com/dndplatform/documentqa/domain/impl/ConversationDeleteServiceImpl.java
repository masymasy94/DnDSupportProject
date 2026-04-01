package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.documentqa.domain.ConversationDeleteService;
import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.repository.ConversationDeleteRepository;
import com.dndplatform.documentqa.domain.repository.ConversationFindRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConversationDeleteServiceImpl implements ConversationDeleteService {

    private final ConversationFindRepository findRepository;
    private final ConversationDeleteRepository deleteRepository;

    @Inject
    public ConversationDeleteServiceImpl(ConversationFindRepository findRepository,
                                         ConversationDeleteRepository deleteRepository) {
        this.findRepository = findRepository;
        this.deleteRepository = deleteRepository;
    }

    @Override
    public void delete(Long id, Long userId) {
        Conversation conversation = findRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Conversation not found: " + id));
        if (!conversation.userId().equals(userId)) {
            throw new ForbiddenException("Access denied to conversation: " + id);
        }
        deleteRepository.deleteById(id);
    }
}

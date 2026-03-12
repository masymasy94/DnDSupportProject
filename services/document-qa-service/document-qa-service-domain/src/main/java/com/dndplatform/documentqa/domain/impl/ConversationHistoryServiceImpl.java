package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.documentqa.domain.ConversationHistoryService;
import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.model.ConversationMessage;
import com.dndplatform.documentqa.domain.repository.ConversationFindRepository;
import com.dndplatform.documentqa.domain.repository.ConversationMessageFindRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ConversationHistoryServiceImpl implements ConversationHistoryService {

    private final ConversationFindRepository conversationRepository;
    private final ConversationMessageFindRepository messageRepository;

    @Inject
    public ConversationHistoryServiceImpl(ConversationFindRepository conversationRepository,
                                          ConversationMessageFindRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public List<ConversationMessage> getHistory(Long conversationId, Long userId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation not found: " + conversationId));
        if (!conversation.userId().equals(userId)) {
            throw new ForbiddenException("Access denied to conversation: " + conversationId);
        }
        return messageRepository.findByConversationId(conversationId);
    }
}

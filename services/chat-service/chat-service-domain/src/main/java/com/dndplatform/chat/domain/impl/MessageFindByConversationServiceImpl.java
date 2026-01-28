package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.MessageFindByConversationService;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.PagedResult;
import com.dndplatform.chat.domain.repository.ConversationFindByIdRepository;
import com.dndplatform.chat.domain.repository.MessageFindByConversationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class MessageFindByConversationServiceImpl implements MessageFindByConversationService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 50;

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MessageFindByConversationRepository messageRepository;
    private final ConversationFindByIdRepository conversationRepository;

    @Inject
    public MessageFindByConversationServiceImpl(MessageFindByConversationRepository messageRepository,
                                                ConversationFindByIdRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public PagedResult<Message> findByConversationId(Long conversationId, Long userId, int page, int pageSize) {
        log.info(() -> "Finding messages: conversationId=%d, userId=%d, page=%d, pageSize=%d"
                .formatted(conversationId, userId, page, pageSize));

        // Verify conversation exists and user is participant
        var conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found: " + conversationId));

        boolean isParticipant = conversation.participants() != null &&
                conversation.participants().stream()
                        .anyMatch(p -> p.userId().equals(userId) && p.leftAt() == null);

        if (!isParticipant) {
            throw new IllegalArgumentException("User is not a participant in this conversation");
        }

        int effectivePage = page < 0 ? DEFAULT_PAGE : page;
        int effectivePageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;

        return messageRepository.findByConversationId(conversationId, effectivePage, effectivePageSize);
    }
}

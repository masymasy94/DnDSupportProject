package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.MessageSendService;
import com.dndplatform.chat.domain.model.Conversation;
import com.dndplatform.chat.domain.model.Message;
import com.dndplatform.chat.domain.model.MessageBuilder;
import com.dndplatform.chat.domain.model.MessageType;
import com.dndplatform.chat.domain.repository.ConversationFindByIdRepository;
import com.dndplatform.chat.domain.repository.MessageCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class MessageSendServiceImpl implements MessageSendService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MessageCreateRepository messageRepository;
    private final ConversationFindByIdRepository conversationRepository;

    @Inject
    public MessageSendServiceImpl(MessageCreateRepository messageRepository,
                                  ConversationFindByIdRepository conversationRepository) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }

    @Override
    public Message send(Long conversationId, Long senderId, String content, MessageType messageType) {
        log.info(() -> "Sending message: conversationId=%d, senderId=%d".formatted(conversationId, senderId));

        conversationRepository.findById(conversationId)
                .filter(c -> isParticipant(senderId, c))
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found or user is not a participant: " + conversationId));

        return messageRepository.create(getMessage(conversationId, senderId, content, messageType));
    }


    private static boolean isParticipant(Long senderId, Conversation conversation) {
        return conversation.participants() != null &&
                conversation.participants().stream()
                        .anyMatch(p -> p.userId().equals(senderId) && p.leftAt() == null);
    }


    private static Message getMessage(Long conversationId, Long senderId, String content, MessageType messageType) {
        return MessageBuilder.builder()
                .withConversationId(conversationId)
                .withSenderId(senderId)
                .withContent(content)
                .withMessageType(messageType != null ? messageType : MessageType.TEXT)
                .withCreatedAt(LocalDateTime.now())
                .build();
    }
}

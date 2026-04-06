package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationMessageEntity;
import com.dndplatform.documentqa.domain.model.ConversationMessage;
import com.dndplatform.documentqa.domain.model.ConversationMessageBuilder;
import com.dndplatform.documentqa.domain.repository.ConversationMessageCreateRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class ConversationMessageCreateRepositoryJpa implements ConversationMessageCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final ConversationPanacheRepository conversationPanache;
    private final ConversationMessagePanacheRepository messagePanache;

    @Inject
    public ConversationMessageCreateRepositoryJpa(
            ConversationPanacheRepository conversationPanache,
            ConversationMessagePanacheRepository messagePanache) {
        this.conversationPanache = conversationPanache;
        this.messagePanache = messagePanache;
    }

    @Override
    @Transactional
    public ConversationMessage create(Long conversationId, String role, String content) {
        log.info(() -> "Creating message for conversation %d".formatted(conversationId));

        ConversationEntity conversation = conversationPanache.findByIdOptional(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation not found with ID: %d".formatted(conversationId)));

        ConversationMessageEntity entity = new ConversationMessageEntity();
        entity.conversation = conversation;
        entity.role = role;
        entity.content = content;

        messagePanache.persist(entity);

        log.info(() -> "Message created with ID: %d".formatted(entity.id));
        return toDomain(entity);
    }

    private ConversationMessage toDomain(ConversationMessageEntity entity) {
        return ConversationMessageBuilder.builder()
                .withId(entity.id)
                .withConversationId(entity.conversation.id)
                .withRole(entity.role)
                .withContent(entity.content)
                .withCreatedAt(entity.createdAt)
                .build();
    }
}

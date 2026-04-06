package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationMessageEntity;
import com.dndplatform.documentqa.domain.model.ConversationMessage;
import com.dndplatform.documentqa.domain.model.ConversationMessageBuilder;
import com.dndplatform.documentqa.domain.repository.ConversationMessageFindRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ConversationMessageFindRepositoryJpa implements ConversationMessageFindRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final ConversationMessagePanacheRepository panacheRepository;

    @Inject
    public ConversationMessageFindRepositoryJpa(ConversationMessagePanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public List<ConversationMessage> findByConversationId(Long conversationId) {
        log.info(() -> "Finding messages for conversation: %d".formatted(conversationId));

        List<ConversationMessageEntity> entities = panacheRepository.findByConversationId(conversationId);

        return entities.stream().map(this::toDomain).toList();
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

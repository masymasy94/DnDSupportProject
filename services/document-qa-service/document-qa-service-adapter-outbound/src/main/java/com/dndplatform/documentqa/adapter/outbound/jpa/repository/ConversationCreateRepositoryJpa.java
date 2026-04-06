package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.model.ConversationBuilder;
import com.dndplatform.documentqa.domain.repository.ConversationCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class ConversationCreateRepositoryJpa implements ConversationCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final ConversationPanacheRepository panacheRepository;

    @Inject
    public ConversationCreateRepositoryJpa(ConversationPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public Conversation create(Long userId, Long campaignId, String title) {
        log.info(() -> "Creating conversation for user %d".formatted(userId));

        ConversationEntity entity = new ConversationEntity();
        entity.userId = userId;
        entity.campaignId = campaignId;
        entity.title = title;

        panacheRepository.persist(entity);

        log.info(() -> "Conversation created with ID: %d".formatted(entity.id));
        return toDomain(entity);
    }

    private Conversation toDomain(ConversationEntity entity) {
        return ConversationBuilder.builder()
                .withId(entity.id)
                .withUserId(entity.userId)
                .withCampaignId(entity.campaignId)
                .withTitle(entity.title)
                .withCreatedAt(entity.createdAt)
                .withUpdatedAt(entity.updatedAt)
                .build();
    }
}

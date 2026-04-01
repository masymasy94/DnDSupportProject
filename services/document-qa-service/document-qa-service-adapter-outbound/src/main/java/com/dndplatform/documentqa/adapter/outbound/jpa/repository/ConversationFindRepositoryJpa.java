package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.documentqa.domain.model.Conversation;
import com.dndplatform.documentqa.domain.model.ConversationBuilder;
import com.dndplatform.documentqa.domain.repository.ConversationFindRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ConversationFindRepositoryJpa implements ConversationFindRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public List<Conversation> findByUserId(Long userId) {
        log.info(() -> "Finding conversations for user: %d".formatted(userId));

        List<ConversationEntity> entities = ConversationEntity.find("userId", userId).list();
        return entities.stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Conversation> findById(Long id) {
        log.info(() -> "Finding conversation by ID: %d".formatted(id));

        ConversationEntity entity = ConversationEntity.findById(id);
        return Optional.ofNullable(entity).map(this::toDomain);
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

package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestUpdate;
import com.dndplatform.campaign.domain.repository.CampaignQuestUpdateRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignQuestUpdateRepositoryJpa implements CampaignQuestUpdateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignQuestUpdateRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CampaignQuest update(CampaignQuestUpdate input) {
        log.info(() -> "Updating quest: %d".formatted(input.id()));

        CampaignQuestEntity entity = CampaignQuestEntity.findById(input.id());
        if (entity == null) {
            throw new NotFoundException("Quest not found with ID: %d".formatted(input.id()));
        }

        if (input.title() != null) {
            entity.title = input.title();
        }
        if (input.description() != null) {
            entity.description = input.description();
        }
        if (input.status() != null) {
            entity.status = input.status().name();
        }
        if (input.priority() != null) {
            entity.priority = input.priority().name();
        }
        entity.updatedAt = LocalDateTime.now();

        entity.persist();

        log.info(() -> "Quest %d updated successfully".formatted(input.id()));
        return mapper.toCampaignQuest(entity);
    }
}

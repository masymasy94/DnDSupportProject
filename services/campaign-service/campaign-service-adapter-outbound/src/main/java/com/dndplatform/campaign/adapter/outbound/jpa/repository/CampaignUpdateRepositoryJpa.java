package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignUpdate;
import com.dndplatform.campaign.domain.repository.CampaignUpdateRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignUpdateRepositoryJpa implements CampaignUpdateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignUpdateRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Campaign update(CampaignUpdate input) {
        log.info(() -> "Updating campaign: %d".formatted(input.id()));

        CampaignEntity entity = CampaignEntity.findById(input.id());
        if (entity == null) {
            throw new NotFoundException("Campaign not found with ID: %d".formatted(input.id()));
        }

        if (input.name() != null) {
            entity.name = input.name();
        }
        if (input.description() != null) {
            entity.description = input.description();
        }
        if (input.status() != null) {
            entity.status = input.status().name();
        }
        if (input.maxPlayers() != null) {
            entity.maxPlayers = input.maxPlayers();
        }
        if (input.imageUrl() != null) {
            entity.imageUrl = input.imageUrl();
        }
        entity.updatedAt = LocalDateTime.now();

        entity.persist();

        log.info(() -> "Campaign %d updated successfully".formatted(input.id()));
        return mapper.toCampaign(entity);
    }
}

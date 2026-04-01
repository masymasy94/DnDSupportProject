package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestCreate;
import com.dndplatform.campaign.domain.repository.CampaignQuestCreateRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignQuestCreateRepositoryJpa implements CampaignQuestCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignQuestCreateRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CampaignQuest save(CampaignQuestCreate input) {
        log.info(() -> "Saving quest for campaign %d".formatted(input.campaignId()));

        CampaignEntity campaign = CampaignEntity.findById(input.campaignId());
        if (campaign == null) {
            throw new NotFoundException("Campaign not found with ID: %d".formatted(input.campaignId()));
        }

        CampaignQuestEntity entity = new CampaignQuestEntity();
        entity.campaign = campaign;
        entity.authorId = input.authorId();
        entity.title = input.title();
        entity.description = input.description();
        entity.status = input.status().name();
        entity.priority = input.priority().name();
        entity.createdAt = LocalDateTime.now();

        entity.persist();

        log.info(() -> "Quest saved with ID: %d".formatted(entity.id));
        return mapper.toCampaignQuest(entity);
    }
}

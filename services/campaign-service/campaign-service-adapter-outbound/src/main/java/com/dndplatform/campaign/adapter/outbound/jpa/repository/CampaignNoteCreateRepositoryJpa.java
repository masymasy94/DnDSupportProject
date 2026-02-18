package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteCreate;
import com.dndplatform.campaign.domain.repository.CampaignNoteCreateRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignNoteCreateRepositoryJpa implements CampaignNoteCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignNoteCreateRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CampaignNote save(CampaignNoteCreate input) {
        log.info(() -> "Saving note for campaign %d".formatted(input.campaignId()));

        CampaignEntity campaign = CampaignEntity.findById(input.campaignId());
        if (campaign == null) {
            throw new NotFoundException("Campaign not found with ID: %d".formatted(input.campaignId()));
        }

        CampaignNoteEntity entity = new CampaignNoteEntity();
        entity.campaign = campaign;
        entity.authorId = input.authorId();
        entity.title = input.title();
        entity.content = input.content();
        entity.visibility = input.visibility().name();
        entity.createdAt = LocalDateTime.now();

        entity.persist();

        log.info(() -> "Note saved with ID: %d".formatted(entity.id));
        return mapper.toCampaignNote(entity);
    }
}

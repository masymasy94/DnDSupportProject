package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignCreate;
import com.dndplatform.campaign.domain.repository.CampaignCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignCreateRepositoryJpa implements CampaignCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignPanacheRepository panacheRepository;
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignCreateRepositoryJpa(CampaignPanacheRepository panacheRepository,
                                       CampaignEntityMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Campaign save(CampaignCreate input) {
        log.info(() -> "Saving campaign: %s".formatted(input.name()));

        CampaignEntity entity = new CampaignEntity();
        entity.name = input.name();
        entity.description = input.description();
        entity.dungeonMasterId = input.dungeonMasterId();
        entity.status = "DRAFT";
        entity.maxPlayers = input.maxPlayers() != null ? input.maxPlayers() : 6;
        entity.imageUrl = input.imageUrl();
        entity.createdAt = LocalDateTime.now();

        panacheRepository.persist(entity);

        log.info(() -> "Campaign saved with ID: %d".formatted(entity.id));
        return mapper.toCampaign(entity);
    }
}

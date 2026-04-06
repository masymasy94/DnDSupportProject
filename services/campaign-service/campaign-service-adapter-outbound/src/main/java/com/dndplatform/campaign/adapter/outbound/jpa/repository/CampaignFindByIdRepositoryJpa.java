package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignFindByIdRepositoryJpa implements CampaignFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignPanacheRepository panacheRepository;
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignFindByIdRepositoryJpa(CampaignPanacheRepository panacheRepository,
                                         CampaignEntityMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Campaign> findById(Long id) {
        log.info(() -> "Finding campaign by ID: %d".formatted(id));

        return panacheRepository.findByIdOptional(id).map(mapper::toCampaign);
    }
}

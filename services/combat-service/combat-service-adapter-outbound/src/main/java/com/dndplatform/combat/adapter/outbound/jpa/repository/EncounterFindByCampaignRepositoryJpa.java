package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.repository.EncounterFindByCampaignRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class EncounterFindByCampaignRepositoryJpa implements EncounterFindByCampaignRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterEntityMapper mapper;
    private final EncounterPanacheRepository panacheRepository;

    @Inject
    public EncounterFindByCampaignRepositoryJpa(EncounterEntityMapper mapper,
                                                EncounterPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public List<Encounter> findByCampaign(Long campaignId) {
        log.info(() -> "Finding encounters for campaign: %d".formatted(campaignId));

        return panacheRepository.findByCampaignId(campaignId)
                .stream()
                .map(mapper::toEncounter)
                .toList();
    }
}

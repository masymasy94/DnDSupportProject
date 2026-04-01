package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.repository.EncounterFindByCampaignRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class EncounterFindByCampaignServiceImpl implements EncounterFindByCampaignService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterFindByCampaignRepository repository;

    @Inject
    public EncounterFindByCampaignServiceImpl(EncounterFindByCampaignRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Encounter> findByCampaign(Long campaignId) {
        log.info(() -> "Finding encounters for campaign: %d".formatted(campaignId));
        return repository.findByCampaign(campaignId);
    }
}

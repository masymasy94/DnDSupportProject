package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
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

    @Inject
    public EncounterFindByCampaignRepositoryJpa(EncounterEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Encounter> findByCampaign(Long campaignId) {
        log.info(() -> "Finding encounters for campaign: %d".formatted(campaignId));

        List<EncounterEntity> entities = EncounterEntity.find("campaignId", campaignId).list();
        return entities.stream().map(mapper::toEncounter).toList();
    }
}

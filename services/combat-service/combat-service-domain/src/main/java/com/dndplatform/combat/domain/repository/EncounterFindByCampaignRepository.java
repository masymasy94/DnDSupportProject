package com.dndplatform.combat.domain.repository;

import com.dndplatform.combat.domain.model.Encounter;

import java.util.List;

public interface EncounterFindByCampaignRepository {
    List<Encounter> findByCampaign(Long campaignId);
}

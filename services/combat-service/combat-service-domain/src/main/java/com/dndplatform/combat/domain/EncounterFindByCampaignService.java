package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;

import java.util.List;

public interface EncounterFindByCampaignService {
    List<Encounter> findByCampaign(Long campaignId);
}

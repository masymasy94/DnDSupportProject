package com.dndplatform.combat.view.model;

import com.dndplatform.combat.view.model.vm.EncounterViewModel;

import java.util.List;

public interface EncounterFindByCampaignResource {
    List<EncounterViewModel> findByCampaign(Long campaignId, Long userId);
}

package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateQuestRequest;
import jakarta.validation.Valid;

public interface CampaignQuestUpdateResource {
    CampaignQuestViewModel update(Long campaignId, Long questId, @Valid UpdateQuestRequest request);
}

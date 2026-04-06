package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.campaign.view.model.vm.CreateQuestRequest;
import jakarta.validation.Valid;

public interface CampaignQuestCreateResource {
    CampaignQuestViewModel create(Long campaignId, @Valid CreateQuestRequest request);
}

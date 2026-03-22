package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;

public interface CampaignQuestFindByIdResource {
    CampaignQuestViewModel findById(Long campaignId, Long questId, Long userId);
}

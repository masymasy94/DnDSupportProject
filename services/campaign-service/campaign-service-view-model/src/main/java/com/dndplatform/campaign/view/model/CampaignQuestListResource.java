package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;

import java.util.List;

public interface CampaignQuestListResource {
    List<CampaignQuestViewModel> list(Long campaignId, Long userId, String status);
}

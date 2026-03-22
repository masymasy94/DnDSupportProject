package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestUpdate;

public interface CampaignQuestUpdateService {
    CampaignQuest update(CampaignQuestUpdate input, Long campaignId, Long userId);
}

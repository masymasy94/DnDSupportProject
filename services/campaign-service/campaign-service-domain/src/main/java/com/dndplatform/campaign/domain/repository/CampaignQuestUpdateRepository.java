package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestUpdate;

public interface CampaignQuestUpdateRepository {
    CampaignQuest update(CampaignQuestUpdate input);
}

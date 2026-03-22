package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestCreate;

public interface CampaignQuestCreateRepository {
    CampaignQuest save(CampaignQuestCreate input);
}

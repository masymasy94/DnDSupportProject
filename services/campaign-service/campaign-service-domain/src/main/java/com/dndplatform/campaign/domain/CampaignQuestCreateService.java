package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestCreate;

public interface CampaignQuestCreateService {
    CampaignQuest create(CampaignQuestCreate input);
}

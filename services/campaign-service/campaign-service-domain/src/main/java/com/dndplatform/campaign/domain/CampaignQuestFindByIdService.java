package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignQuest;

public interface CampaignQuestFindByIdService {
    CampaignQuest findById(Long questId);
}

package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.QuestStatus;

import java.util.List;

public interface CampaignQuestFindByCampaignService {
    List<CampaignQuest> findByCampaign(Long campaignId, QuestStatus status);
}

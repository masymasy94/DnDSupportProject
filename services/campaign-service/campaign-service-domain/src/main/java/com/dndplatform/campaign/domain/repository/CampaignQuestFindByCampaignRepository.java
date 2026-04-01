package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.QuestStatus;

import java.util.List;

public interface CampaignQuestFindByCampaignRepository {
    List<CampaignQuest> findByCampaign(Long campaignId, QuestStatus status);
}

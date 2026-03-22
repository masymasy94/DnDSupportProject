package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignQuest;

import java.util.Optional;

public interface CampaignQuestFindByIdRepository {
    Optional<CampaignQuest> findById(Long questId);
}

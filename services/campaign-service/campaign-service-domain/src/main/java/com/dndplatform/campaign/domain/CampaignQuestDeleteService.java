package com.dndplatform.campaign.domain;

public interface CampaignQuestDeleteService {
    void delete(Long campaignId, Long questId, Long userId);
}

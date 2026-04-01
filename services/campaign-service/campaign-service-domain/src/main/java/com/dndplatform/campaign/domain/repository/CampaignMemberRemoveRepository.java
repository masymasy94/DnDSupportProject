package com.dndplatform.campaign.domain.repository;

public interface CampaignMemberRemoveRepository {
    void remove(Long campaignId, Long userId);
}

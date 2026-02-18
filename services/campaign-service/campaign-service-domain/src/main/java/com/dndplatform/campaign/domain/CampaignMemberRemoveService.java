package com.dndplatform.campaign.domain;

public interface CampaignMemberRemoveService {
    void remove(Long campaignId, Long userId, Long requesterId);
}

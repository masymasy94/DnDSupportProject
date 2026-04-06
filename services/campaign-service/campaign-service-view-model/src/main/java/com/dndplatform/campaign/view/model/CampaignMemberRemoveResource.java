package com.dndplatform.campaign.view.model;

public interface CampaignMemberRemoveResource {
    void removeMember(Long campaignId, Long userId, Long requesterId);
}

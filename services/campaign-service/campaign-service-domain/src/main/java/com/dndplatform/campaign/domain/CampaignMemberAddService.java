package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignMember;

public interface CampaignMemberAddService {
    CampaignMember add(Long campaignId, Long userId, Long characterId, Long requesterId);
}

package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.model.MemberRole;

public interface CampaignMemberAddRepository {
    CampaignMember add(Long campaignId, Long userId, Long characterId, MemberRole role);
}

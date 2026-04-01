package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignMember;

import java.util.Optional;

public interface CampaignMemberFindByUserRepository {
    Optional<CampaignMember> findByCampaignIdAndUserId(Long campaignId, Long userId);
}

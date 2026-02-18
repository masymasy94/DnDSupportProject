package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignMember;

import java.util.List;

public interface CampaignMemberFindByCampaignRepository {
    List<CampaignMember> findByCampaignId(Long campaignId);
}

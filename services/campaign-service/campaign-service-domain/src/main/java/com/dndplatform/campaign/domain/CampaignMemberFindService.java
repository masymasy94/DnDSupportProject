package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignMember;

import java.util.List;

public interface CampaignMemberFindService {
    List<CampaignMember> findByCampaignId(Long campaignId);
}

package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignMemberViewModel;

import java.util.List;

public interface CampaignMemberListResource {
    List<CampaignMemberViewModel> listMembers(Long campaignId);
}

package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.AddMemberRequest;
import com.dndplatform.campaign.view.model.vm.CampaignMemberViewModel;
import jakarta.validation.Valid;

public interface CampaignMemberAddResource {
    CampaignMemberViewModel addMember(Long campaignId, @Valid AddMemberRequest request);
}

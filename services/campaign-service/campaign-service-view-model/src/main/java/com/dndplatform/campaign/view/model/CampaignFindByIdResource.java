package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignViewModel;

public interface CampaignFindByIdResource {
    CampaignViewModel findById(Long id);
}

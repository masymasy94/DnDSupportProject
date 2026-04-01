package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.PagedCampaignsViewModel;

public interface CampaignFindAllResource {
    PagedCampaignsViewModel findAll(Long userId, int page, int size);
}

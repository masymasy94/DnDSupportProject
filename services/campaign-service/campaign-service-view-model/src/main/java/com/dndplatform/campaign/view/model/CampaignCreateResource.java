package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import jakarta.validation.Valid;

public interface CampaignCreateResource {
    CampaignViewModel create(@Valid CreateCampaignRequest request);
}

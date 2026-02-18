package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateCampaignRequest;
import jakarta.validation.Valid;

public interface CampaignUpdateResource {
    CampaignViewModel update(Long id, @Valid UpdateCampaignRequest request);
}

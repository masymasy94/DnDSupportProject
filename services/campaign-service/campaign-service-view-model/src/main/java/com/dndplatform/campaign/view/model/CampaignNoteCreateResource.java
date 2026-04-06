package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.campaign.view.model.vm.CreateNoteRequest;
import jakarta.validation.Valid;

public interface CampaignNoteCreateResource {
    CampaignNoteViewModel create(Long campaignId, @Valid CreateNoteRequest request);
}

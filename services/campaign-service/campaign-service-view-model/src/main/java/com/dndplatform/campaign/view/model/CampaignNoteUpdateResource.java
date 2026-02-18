package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateNoteRequest;
import jakarta.validation.Valid;

public interface CampaignNoteUpdateResource {
    CampaignNoteViewModel update(Long campaignId, Long noteId, @Valid UpdateNoteRequest request);
}

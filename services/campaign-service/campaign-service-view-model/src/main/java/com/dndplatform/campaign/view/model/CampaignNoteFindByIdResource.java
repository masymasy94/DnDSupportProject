package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;

public interface CampaignNoteFindByIdResource {
    CampaignNoteViewModel findById(Long campaignId, Long noteId, Long userId);
}

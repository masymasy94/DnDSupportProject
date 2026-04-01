package com.dndplatform.campaign.view.model;

import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;

import java.util.List;

public interface CampaignNoteListResource {
    List<CampaignNoteViewModel> listNotes(Long campaignId, Long userId);
}

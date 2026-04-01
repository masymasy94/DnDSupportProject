package com.dndplatform.campaign.view.model;

public interface CampaignNoteDeleteResource {
    void delete(Long campaignId, Long noteId, Long userId);
}

package com.dndplatform.campaign.domain;

public interface CampaignNoteDeleteService {
    void delete(Long campaignId, Long noteId, Long userId);
}

package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignNote;

import java.util.List;

public interface CampaignNoteFindVisibleRepository {
    List<CampaignNote> findVisibleNotes(Long campaignId, Long userId);
}

package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignNote;

import java.util.List;

public interface CampaignNoteFindVisibleService {
    List<CampaignNote> findVisibleNotes(Long campaignId, Long userId);
}

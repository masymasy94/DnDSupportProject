package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignNote;

public interface CampaignNoteFindByIdService {
    CampaignNote findById(Long noteId, Long userId);
}

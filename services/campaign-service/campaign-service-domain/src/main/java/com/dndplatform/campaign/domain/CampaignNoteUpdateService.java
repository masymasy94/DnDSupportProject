package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteUpdate;

public interface CampaignNoteUpdateService {
    CampaignNote update(CampaignNoteUpdate input, Long userId);
}

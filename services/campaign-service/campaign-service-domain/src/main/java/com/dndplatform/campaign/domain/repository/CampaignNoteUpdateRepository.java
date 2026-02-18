package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteUpdate;

public interface CampaignNoteUpdateRepository {
    CampaignNote update(CampaignNoteUpdate input);
}

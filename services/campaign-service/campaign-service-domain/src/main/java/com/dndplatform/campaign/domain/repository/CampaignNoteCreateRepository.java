package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteCreate;

public interface CampaignNoteCreateRepository {
    CampaignNote save(CampaignNoteCreate input);
}

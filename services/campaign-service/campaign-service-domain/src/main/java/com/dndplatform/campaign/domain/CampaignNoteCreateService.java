package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteCreate;

public interface CampaignNoteCreateService {
    CampaignNote create(CampaignNoteCreate input);
}

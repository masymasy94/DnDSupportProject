package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignCreate;

public interface CampaignCreateRepository {
    Campaign save(CampaignCreate input);
}

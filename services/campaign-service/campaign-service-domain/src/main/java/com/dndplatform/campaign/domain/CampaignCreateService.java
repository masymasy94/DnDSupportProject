package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignCreate;

public interface CampaignCreateService {
    Campaign create(CampaignCreate input);
}

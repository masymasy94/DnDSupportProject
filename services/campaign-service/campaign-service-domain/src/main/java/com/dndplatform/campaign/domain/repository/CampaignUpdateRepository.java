package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignUpdate;

public interface CampaignUpdateRepository {
    Campaign update(CampaignUpdate input);
}

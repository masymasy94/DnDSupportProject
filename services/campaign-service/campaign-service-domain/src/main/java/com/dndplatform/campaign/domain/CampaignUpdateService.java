package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignUpdate;

public interface CampaignUpdateService {
    Campaign update(CampaignUpdate input, Long userId);
}

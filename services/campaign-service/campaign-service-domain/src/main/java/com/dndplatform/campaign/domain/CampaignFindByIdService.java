package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;

public interface CampaignFindByIdService {
    Campaign findById(Long id);
}

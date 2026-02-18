package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.PagedResult;

public interface CampaignFindByUserService {
    PagedResult findByUserId(Long userId, int page, int size);
}

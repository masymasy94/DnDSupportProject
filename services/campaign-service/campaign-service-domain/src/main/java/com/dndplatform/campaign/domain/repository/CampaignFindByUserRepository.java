package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.PagedResult;

public interface CampaignFindByUserRepository {
    PagedResult findByUserId(Long userId, int page, int size);
}

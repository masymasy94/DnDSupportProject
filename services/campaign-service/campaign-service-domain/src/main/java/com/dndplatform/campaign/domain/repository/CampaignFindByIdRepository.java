package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.Campaign;

import java.util.Optional;

public interface CampaignFindByIdRepository {
    Optional<Campaign> findById(Long id);
}

package com.dndplatform.campaign.domain.repository;

import com.dndplatform.campaign.domain.model.CampaignNote;

import java.util.Optional;

public interface CampaignNoteFindByIdRepository {
    Optional<CampaignNote> findById(Long noteId);
}

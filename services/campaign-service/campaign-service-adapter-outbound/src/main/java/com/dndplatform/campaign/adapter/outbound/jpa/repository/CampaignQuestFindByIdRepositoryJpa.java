package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class CampaignQuestFindByIdRepositoryJpa implements CampaignQuestFindByIdRepository {

    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignQuestFindByIdRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<CampaignQuest> findById(Long questId) {
        CampaignQuestEntity entity = CampaignQuestEntity.findById(questId);
        return Optional.ofNullable(entity).map(mapper::toCampaignQuest);
    }
}

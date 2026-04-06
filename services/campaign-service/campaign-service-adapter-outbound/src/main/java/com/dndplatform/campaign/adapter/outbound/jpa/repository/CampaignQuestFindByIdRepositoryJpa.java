package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class CampaignQuestFindByIdRepositoryJpa implements CampaignQuestFindByIdRepository {

    private final CampaignQuestPanacheRepository panacheRepository;
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignQuestFindByIdRepositoryJpa(CampaignQuestPanacheRepository panacheRepository,
                                              CampaignEntityMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CampaignQuest> findById(Long questId) {
        return panacheRepository.findByIdOptional(questId).map(mapper::toCampaignQuest);
    }
}

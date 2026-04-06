package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignQuestEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByCampaignRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignQuestFindByCampaignRepositoryJpa implements CampaignQuestFindByCampaignRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignQuestPanacheRepository panacheRepository;
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignQuestFindByCampaignRepositoryJpa(CampaignQuestPanacheRepository panacheRepository,
                                                    CampaignEntityMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CampaignQuest> findByCampaign(Long campaignId, QuestStatus status) {
        log.info(() -> "Finding quests for campaign %d with status %s".formatted(campaignId, status));

        List<CampaignQuestEntity> entities;
        if (status == null) {
            entities = panacheRepository.findByCampaignId(campaignId);
        } else {
            entities = panacheRepository.findByCampaignIdAndStatus(campaignId, status.name());
        }

        return entities.stream()
                .map(mapper::toCampaignQuest)
                .toList();
    }
}

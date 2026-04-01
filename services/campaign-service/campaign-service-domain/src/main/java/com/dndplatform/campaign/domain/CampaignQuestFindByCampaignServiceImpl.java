package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByCampaignRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignQuestFindByCampaignServiceImpl implements CampaignQuestFindByCampaignService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignQuestFindByCampaignRepository repository;

    @Inject
    public CampaignQuestFindByCampaignServiceImpl(CampaignQuestFindByCampaignRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CampaignQuest> findByCampaign(Long campaignId, QuestStatus status) {
        log.info(() -> "Finding quests for campaign %d with status %s".formatted(campaignId, status));
        return repository.findByCampaign(campaignId, status);
    }
}

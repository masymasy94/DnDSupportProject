package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestCreate;
import com.dndplatform.campaign.domain.repository.CampaignQuestCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignQuestCreateServiceImpl implements CampaignQuestCreateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignQuestCreateRepository repository;

    @Inject
    public CampaignQuestCreateServiceImpl(CampaignQuestCreateRepository repository) {
        this.repository = repository;
    }

    @Override
    public CampaignQuest create(CampaignQuestCreate input) {
        log.info(() -> "Creating quest for campaign %d by user %d".formatted(input.campaignId(), input.authorId()));

        CampaignQuest quest = repository.save(input);
        log.info(() -> "Quest created with ID: %d".formatted(quest.id()));
        return quest;
    }
}

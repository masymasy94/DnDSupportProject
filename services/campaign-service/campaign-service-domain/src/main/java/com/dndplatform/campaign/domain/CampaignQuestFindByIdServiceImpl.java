package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByIdRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignQuestFindByIdServiceImpl implements CampaignQuestFindByIdService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignQuestFindByIdRepository repository;

    @Inject
    public CampaignQuestFindByIdServiceImpl(CampaignQuestFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public CampaignQuest findById(Long questId) {
        log.info(() -> "Finding quest %d".formatted(questId));

        return repository.findById(questId)
                .orElseThrow(() -> new NotFoundException("Quest not found with ID: %d".formatted(questId)));
    }
}

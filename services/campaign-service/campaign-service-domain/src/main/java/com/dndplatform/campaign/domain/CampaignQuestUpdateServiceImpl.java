package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.model.CampaignQuestUpdate;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignQuestUpdateRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignQuestUpdateServiceImpl implements CampaignQuestUpdateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignFindByIdRepository campaignFindRepository;
    private final CampaignQuestFindByIdRepository questFindByIdRepository;
    private final CampaignQuestUpdateRepository questUpdateRepository;

    @Inject
    public CampaignQuestUpdateServiceImpl(CampaignFindByIdRepository campaignFindRepository,
                                          CampaignQuestFindByIdRepository questFindByIdRepository,
                                          CampaignQuestUpdateRepository questUpdateRepository) {
        this.campaignFindRepository = campaignFindRepository;
        this.questFindByIdRepository = questFindByIdRepository;
        this.questUpdateRepository = questUpdateRepository;
    }

    @Override
    public CampaignQuest update(CampaignQuestUpdate input, Long campaignId, Long userId) {
        log.info(() -> "Updating quest %d in campaign %d by user %d".formatted(input.id(), campaignId, userId));

        CampaignQuest existing = questFindByIdRepository.findById(input.id())
                .orElseThrow(() -> new NotFoundException("Quest not found with ID: %d".formatted(input.id())));

        Campaign campaign = campaignFindRepository.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with ID: %d".formatted(campaignId)));

        boolean isAuthor = existing.authorId().equals(userId);
        boolean isDm = campaign.dungeonMasterId().equals(userId);

        if (!isAuthor && !isDm) {
            throw new ForbiddenException("Only the author or Dungeon Master can update this quest");
        }

        CampaignQuest updated = questUpdateRepository.update(input);
        log.info(() -> "Quest %d updated successfully".formatted(input.id()));
        return updated;
    }
}

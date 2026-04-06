package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignQuestDeleteRepository;
import com.dndplatform.campaign.domain.repository.CampaignQuestFindByIdRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignQuestDeleteServiceImpl implements CampaignQuestDeleteService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignFindByIdRepository campaignFindRepository;
    private final CampaignQuestFindByIdRepository questFindByIdRepository;
    private final CampaignQuestDeleteRepository questDeleteRepository;

    @Inject
    public CampaignQuestDeleteServiceImpl(CampaignFindByIdRepository campaignFindRepository,
                                          CampaignQuestFindByIdRepository questFindByIdRepository,
                                          CampaignQuestDeleteRepository questDeleteRepository) {
        this.campaignFindRepository = campaignFindRepository;
        this.questFindByIdRepository = questFindByIdRepository;
        this.questDeleteRepository = questDeleteRepository;
    }

    @Override
    public void delete(Long campaignId, Long questId, Long userId) {
        log.info(() -> "Deleting quest %d from campaign %d by user %d".formatted(questId, campaignId, userId));

        CampaignQuest quest = questFindByIdRepository.findById(questId)
                .orElseThrow(() -> new NotFoundException("Quest not found with ID: %d".formatted(questId)));

        Campaign campaign = campaignFindRepository.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with ID: %d".formatted(campaignId)));

        boolean isAuthor = quest.authorId().equals(userId);
        boolean isDm = campaign.dungeonMasterId().equals(userId);

        if (!isAuthor && !isDm) {
            throw new ForbiddenException("Only the author or Dungeon Master can delete this quest");
        }

        questDeleteRepository.deleteById(questId);
        log.info(() -> "Quest %d deleted successfully".formatted(questId));
    }
}

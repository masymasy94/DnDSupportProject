package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignMemberRemoveRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignMemberRemoveServiceImpl implements CampaignMemberRemoveService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignFindByIdRepository campaignFindRepository;
    private final CampaignMemberRemoveRepository memberRemoveRepository;

    @Inject
    public CampaignMemberRemoveServiceImpl(CampaignFindByIdRepository campaignFindRepository,
                                           CampaignMemberRemoveRepository memberRemoveRepository) {
        this.campaignFindRepository = campaignFindRepository;
        this.memberRemoveRepository = memberRemoveRepository;
    }

    @Override
    public void remove(Long campaignId, Long userId, Long requesterId) {
        log.info(() -> "Removing user %d from campaign %d".formatted(userId, campaignId));

        Campaign campaign = campaignFindRepository.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with ID: %d".formatted(campaignId)));

        boolean isDm = campaign.dungeonMasterId().equals(requesterId);
        boolean isSelfLeave = userId.equals(requesterId);

        if (!isDm && !isSelfLeave) {
            throw new ForbiddenException("Only the Dungeon Master or the member themselves can remove a member");
        }

        memberRemoveRepository.remove(campaignId, userId);
        log.info(() -> "User %d removed from campaign %d".formatted(userId, campaignId));
    }
}

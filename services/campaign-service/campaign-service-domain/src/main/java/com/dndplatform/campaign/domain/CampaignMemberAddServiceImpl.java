package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignMember;
import com.dndplatform.campaign.domain.model.MemberRole;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignMemberAddRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignMemberAddServiceImpl implements CampaignMemberAddService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignFindByIdRepository campaignFindRepository;
    private final CampaignMemberAddRepository memberAddRepository;

    @Inject
    public CampaignMemberAddServiceImpl(CampaignFindByIdRepository campaignFindRepository,
                                        CampaignMemberAddRepository memberAddRepository) {
        this.campaignFindRepository = campaignFindRepository;
        this.memberAddRepository = memberAddRepository;
    }

    @Override
    public CampaignMember add(Long campaignId, Long userId, Long characterId, Long requesterId) {
        log.info(() -> "Adding user %d to campaign %d".formatted(userId, campaignId));

        Campaign campaign = campaignFindRepository.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with ID: %d".formatted(campaignId)));

        if (!campaign.dungeonMasterId().equals(requesterId)) {
            throw new ForbiddenException("Only the Dungeon Master can add members to this campaign");
        }

        CampaignMember member = memberAddRepository.add(campaignId, userId, characterId, MemberRole.PLAYER);
        log.info(() -> "User %d added to campaign %d".formatted(userId, campaignId));
        return member;
    }
}

package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.domain.repository.CampaignMemberRemoveRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignMemberRemoveRepositoryJpa implements CampaignMemberRemoveRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignMemberPanacheRepository panacheRepository;

    @Inject
    public CampaignMemberRemoveRepositoryJpa(CampaignMemberPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void remove(Long campaignId, Long userId) {
        log.info(() -> "Removing user %d from campaign %d".formatted(userId, campaignId));

        long deleted = panacheRepository.deleteByCampaignIdAndUserId(campaignId, userId);
        if (deleted == 0) {
            throw new NotFoundException("Member not found in campaign %d for user %d".formatted(campaignId, userId));
        }

        log.info(() -> "User %d removed from campaign %d".formatted(userId, campaignId));
    }
}

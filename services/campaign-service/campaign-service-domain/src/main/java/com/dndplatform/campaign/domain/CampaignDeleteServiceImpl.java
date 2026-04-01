package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.repository.CampaignDeleteRepository;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignDeleteServiceImpl implements CampaignDeleteService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignFindByIdRepository findRepository;
    private final CampaignDeleteRepository deleteRepository;

    @Inject
    public CampaignDeleteServiceImpl(CampaignFindByIdRepository findRepository,
                                     CampaignDeleteRepository deleteRepository) {
        this.findRepository = findRepository;
        this.deleteRepository = deleteRepository;
    }

    @Override
    public void delete(Long campaignId, Long userId) {
        log.info(() -> "Deleting campaign %d by user %d".formatted(campaignId, userId));

        Campaign existing = findRepository.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with ID: %d".formatted(campaignId)));

        if (!existing.dungeonMasterId().equals(userId)) {
            throw new ForbiddenException("Only the Dungeon Master can delete this campaign");
        }

        deleteRepository.deleteById(campaignId);
        log.info(() -> "Campaign %d deleted successfully".formatted(campaignId));
    }
}

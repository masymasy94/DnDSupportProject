package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignUpdate;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignUpdateRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignUpdateServiceImpl implements CampaignUpdateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignFindByIdRepository findRepository;
    private final CampaignUpdateRepository updateRepository;

    @Inject
    public CampaignUpdateServiceImpl(CampaignFindByIdRepository findRepository,
                                     CampaignUpdateRepository updateRepository) {
        this.findRepository = findRepository;
        this.updateRepository = updateRepository;
    }

    @Override
    public Campaign update(CampaignUpdate input, Long userId) {
        log.info(() -> "Updating campaign %d by user %d".formatted(input.id(), userId));

        Campaign existing = findRepository.findById(input.id())
                .orElseThrow(() -> new NotFoundException("Campaign not found with ID: %d".formatted(input.id())));

        if (!existing.dungeonMasterId().equals(userId)) {
            throw new ForbiddenException("Only the Dungeon Master can update this campaign");
        }

        Campaign updated = updateRepository.update(input);
        log.info(() -> "Campaign %d updated successfully".formatted(input.id()));
        return updated;
    }
}

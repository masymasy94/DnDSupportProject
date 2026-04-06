package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.repository.CampaignNoteFindVisibleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignNoteFindVisibleServiceImpl implements CampaignNoteFindVisibleService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignNoteFindVisibleRepository repository;

    @Inject
    public CampaignNoteFindVisibleServiceImpl(CampaignNoteFindVisibleRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CampaignNote> findVisibleNotes(Long campaignId, Long userId) {
        log.info(() -> "Finding visible notes for campaign %d, user %d".formatted(campaignId, userId));
        return repository.findVisibleNotes(campaignId, userId);
    }
}

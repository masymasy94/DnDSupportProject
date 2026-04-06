package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.repository.CampaignNoteFindVisibleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignNoteFindVisibleRepositoryJpa implements CampaignNoteFindVisibleRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignNotePanacheRepository panacheRepository;
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignNoteFindVisibleRepositoryJpa(CampaignNotePanacheRepository panacheRepository,
                                                CampaignEntityMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CampaignNote> findVisibleNotes(Long campaignId, Long userId) {
        log.info(() -> "Finding visible notes for campaign %d, user %d".formatted(campaignId, userId));

        return panacheRepository.findVisibleNotes(campaignId, userId).stream()
                .map(mapper::toCampaignNote)
                .toList();
    }
}

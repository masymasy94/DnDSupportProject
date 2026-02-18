package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
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
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignNoteFindVisibleRepositoryJpa(CampaignEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<CampaignNote> findVisibleNotes(Long campaignId, Long userId) {
        log.info(() -> "Finding visible notes for campaign %d, user %d".formatted(campaignId, userId));

        return CampaignNoteEntity.<CampaignNoteEntity>find(
                "campaign.id = ?1 and (visibility = 'PUBLIC' or authorId = ?2)",
                campaignId, userId
        ).list().stream()
                .map(mapper::toCampaignNote)
                .toList();
    }
}

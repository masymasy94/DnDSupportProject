package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.adapter.outbound.jpa.mapper.CampaignEntityMapper;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteUpdate;
import com.dndplatform.campaign.domain.repository.CampaignNoteUpdateRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class CampaignNoteUpdateRepositoryJpa implements CampaignNoteUpdateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignNotePanacheRepository panacheRepository;
    private final CampaignEntityMapper mapper;

    @Inject
    public CampaignNoteUpdateRepositoryJpa(CampaignNotePanacheRepository panacheRepository,
                                           CampaignEntityMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public CampaignNote update(CampaignNoteUpdate input) {
        log.info(() -> "Updating note: %d".formatted(input.id()));

        CampaignNoteEntity entity = panacheRepository.findById(input.id());
        if (entity == null) {
            throw new NotFoundException("Note not found with ID: %d".formatted(input.id()));
        }

        if (input.title() != null) {
            entity.title = input.title();
        }
        if (input.content() != null) {
            entity.content = input.content();
        }
        if (input.visibility() != null) {
            entity.visibility = input.visibility().name();
        }
        entity.updatedAt = LocalDateTime.now();

        panacheRepository.persist(entity);

        log.info(() -> "Note %d updated successfully".formatted(input.id()));
        return mapper.toCampaignNote(entity);
    }
}

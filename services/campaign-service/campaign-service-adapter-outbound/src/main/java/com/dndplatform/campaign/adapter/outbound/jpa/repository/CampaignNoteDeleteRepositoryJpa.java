package com.dndplatform.campaign.adapter.outbound.jpa.repository;

import com.dndplatform.campaign.adapter.outbound.jpa.entity.CampaignNoteEntity;
import com.dndplatform.campaign.domain.repository.CampaignNoteDeleteRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignNoteDeleteRepositoryJpa implements CampaignNoteDeleteRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignNotePanacheRepository panacheRepository;

    @Inject
    public CampaignNoteDeleteRepositoryJpa(CampaignNotePanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void deleteById(Long noteId) {
        log.info(() -> "Deleting note: %d".formatted(noteId));

        CampaignNoteEntity entity = panacheRepository.findById(noteId);
        if (entity == null) {
            throw new NotFoundException("Note not found with ID: %d".formatted(noteId));
        }

        panacheRepository.delete(entity);
        log.info(() -> "Note %d deleted successfully".formatted(noteId));
    }
}

package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.repository.CampaignFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignNoteDeleteRepository;
import com.dndplatform.campaign.domain.repository.CampaignNoteFindByIdRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignNoteDeleteServiceImpl implements CampaignNoteDeleteService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignFindByIdRepository campaignFindRepository;
    private final CampaignNoteFindByIdRepository noteFindByIdRepository;
    private final CampaignNoteDeleteRepository noteDeleteRepository;

    @Inject
    public CampaignNoteDeleteServiceImpl(CampaignFindByIdRepository campaignFindRepository,
                                         CampaignNoteFindByIdRepository noteFindByIdRepository,
                                         CampaignNoteDeleteRepository noteDeleteRepository) {
        this.campaignFindRepository = campaignFindRepository;
        this.noteFindByIdRepository = noteFindByIdRepository;
        this.noteDeleteRepository = noteDeleteRepository;
    }

    @Override
    public void delete(Long campaignId, Long noteId, Long userId) {
        log.info(() -> "Deleting note %d from campaign %d by user %d".formatted(noteId, campaignId, userId));

        CampaignNote note = noteFindByIdRepository.findById(noteId)
                .orElseThrow(() -> new NotFoundException("Note not found with ID: %d".formatted(noteId)));

        Campaign campaign = campaignFindRepository.findById(campaignId)
                .orElseThrow(() -> new NotFoundException("Campaign not found with ID: %d".formatted(campaignId)));

        boolean isAuthor = note.authorId().equals(userId);
        boolean isDm = campaign.dungeonMasterId().equals(userId);

        if (!isAuthor && !isDm) {
            throw new ForbiddenException("Only the author or Dungeon Master can delete this note");
        }

        noteDeleteRepository.deleteById(noteId);
        log.info(() -> "Note %d deleted successfully".formatted(noteId));
    }
}

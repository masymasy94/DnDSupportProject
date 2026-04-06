package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteUpdate;
import com.dndplatform.campaign.domain.repository.CampaignNoteFindByIdRepository;
import com.dndplatform.campaign.domain.repository.CampaignNoteUpdateRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignNoteUpdateServiceImpl implements CampaignNoteUpdateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignNoteFindByIdRepository noteFindByIdRepository;
    private final CampaignNoteUpdateRepository noteUpdateRepository;

    @Inject
    public CampaignNoteUpdateServiceImpl(CampaignNoteFindByIdRepository noteFindByIdRepository,
                                         CampaignNoteUpdateRepository noteUpdateRepository) {
        this.noteFindByIdRepository = noteFindByIdRepository;
        this.noteUpdateRepository = noteUpdateRepository;
    }

    @Override
    public CampaignNote update(CampaignNoteUpdate input, Long userId) {
        log.info(() -> "Updating note %d by user %d".formatted(input.id(), userId));

        CampaignNote existing = noteFindByIdRepository.findById(input.id())
                .orElseThrow(() -> new NotFoundException("Note not found with ID: %d".formatted(input.id())));

        if (!existing.authorId().equals(userId)) {
            throw new ForbiddenException("Only the author can update this note");
        }

        CampaignNote updated = noteUpdateRepository.update(input);
        log.info(() -> "Note %d updated successfully".formatted(input.id()));
        return updated;
    }
}

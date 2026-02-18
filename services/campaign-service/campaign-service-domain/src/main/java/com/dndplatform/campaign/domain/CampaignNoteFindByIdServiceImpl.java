package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.NoteVisibility;
import com.dndplatform.campaign.domain.repository.CampaignNoteFindByIdRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignNoteFindByIdServiceImpl implements CampaignNoteFindByIdService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignNoteFindByIdRepository repository;

    @Inject
    public CampaignNoteFindByIdServiceImpl(CampaignNoteFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public CampaignNote findById(Long noteId, Long userId) {
        log.info(() -> "Finding note %d for user %d".formatted(noteId, userId));

        CampaignNote note = repository.findById(noteId)
                .orElseThrow(() -> new NotFoundException("Note not found with ID: %d".formatted(noteId)));

        if (note.visibility() == NoteVisibility.PRIVATE && !note.authorId().equals(userId)) {
            throw new ForbiddenException("You do not have access to this private note");
        }

        return note;
    }
}

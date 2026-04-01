package com.dndplatform.campaign.domain;

import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteCreate;
import com.dndplatform.campaign.domain.repository.CampaignNoteCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class CampaignNoteCreateServiceImpl implements CampaignNoteCreateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final CampaignNoteCreateRepository repository;

    @Inject
    public CampaignNoteCreateServiceImpl(CampaignNoteCreateRepository repository) {
        this.repository = repository;
    }

    @Override
    public CampaignNote create(CampaignNoteCreate input) {
        log.info(() -> "Creating note for campaign %d by user %d".formatted(input.campaignId(), input.authorId()));

        CampaignNote note = repository.save(input);
        log.info(() -> "Note created with ID: %d".formatted(note.id()));
        return note;
    }
}

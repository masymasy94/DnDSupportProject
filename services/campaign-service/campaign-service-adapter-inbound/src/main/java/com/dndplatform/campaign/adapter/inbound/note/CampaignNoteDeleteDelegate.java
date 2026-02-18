package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.domain.CampaignNoteDeleteService;
import com.dndplatform.campaign.view.model.CampaignNoteDeleteResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignNoteDeleteDelegate implements CampaignNoteDeleteResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignNoteDeleteService service;

    @Inject
    public CampaignNoteDeleteDelegate(CampaignNoteDeleteService service) {
        this.service = service;
    }

    @Override
    public void delete(Long campaignId, Long noteId, Long userId) {
        log.info(() -> "Deleting note %d from campaign %d by user %d".formatted(noteId, campaignId, userId));
        service.delete(campaignId, noteId, userId);
    }
}

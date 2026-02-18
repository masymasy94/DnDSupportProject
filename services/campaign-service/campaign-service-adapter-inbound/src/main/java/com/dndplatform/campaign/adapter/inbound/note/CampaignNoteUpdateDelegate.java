package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.adapter.inbound.note.mapper.CampaignNoteViewModelMapper;
import com.dndplatform.campaign.adapter.inbound.note.mapper.UpdateNoteMapper;
import com.dndplatform.campaign.domain.CampaignNoteUpdateService;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteUpdate;
import com.dndplatform.campaign.view.model.CampaignNoteUpdateResource;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateNoteRequest;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignNoteUpdateDelegate implements CampaignNoteUpdateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignNoteUpdateService service;
    private final CampaignNoteViewModelMapper viewModelMapper;
    private final UpdateNoteMapper updateMapper;

    @Inject
    public CampaignNoteUpdateDelegate(CampaignNoteUpdateService service,
                                      CampaignNoteViewModelMapper viewModelMapper,
                                      UpdateNoteMapper updateMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
        this.updateMapper = updateMapper;
    }

    @Override
    public CampaignNoteViewModel update(Long campaignId, Long noteId, UpdateNoteRequest request) {
        Long userId = request.userId();
        log.info(() -> "Updating note %d in campaign %d by user %d".formatted(noteId, campaignId, userId));

        CampaignNoteUpdate domainUpdate = updateMapper.apply(noteId, request);
        CampaignNote note = service.update(domainUpdate, userId);
        return viewModelMapper.apply(note);
    }
}

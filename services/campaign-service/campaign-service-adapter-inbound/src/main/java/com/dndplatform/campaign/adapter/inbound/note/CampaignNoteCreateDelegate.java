package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.adapter.inbound.note.mapper.CampaignNoteViewModelMapper;
import com.dndplatform.campaign.adapter.inbound.note.mapper.CreateNoteMapper;
import com.dndplatform.campaign.domain.CampaignNoteCreateService;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.domain.model.CampaignNoteCreate;
import com.dndplatform.campaign.view.model.CampaignNoteCreateResource;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.campaign.view.model.vm.CreateNoteRequest;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignNoteCreateDelegate implements CampaignNoteCreateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignNoteCreateService service;
    private final CampaignNoteViewModelMapper viewModelMapper;
    private final CreateNoteMapper createMapper;

    @Inject
    public CampaignNoteCreateDelegate(CampaignNoteCreateService service,
                                      CampaignNoteViewModelMapper viewModelMapper,
                                      CreateNoteMapper createMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
        this.createMapper = createMapper;
    }

    @Override
    public CampaignNoteViewModel create(Long campaignId, CreateNoteRequest request) {
        Long userId = request.userId();
        log.info(() -> "Creating note for campaign %d by user %d".formatted(campaignId, userId));

        CampaignNoteCreate domainCreate = createMapper.apply(campaignId, request, userId);
        CampaignNote note = service.create(domainCreate);
        return viewModelMapper.apply(note);
    }
}

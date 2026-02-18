package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.adapter.inbound.note.mapper.CampaignNoteViewModelMapper;
import com.dndplatform.campaign.domain.CampaignNoteFindByIdService;
import com.dndplatform.campaign.domain.model.CampaignNote;
import com.dndplatform.campaign.view.model.CampaignNoteFindByIdResource;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignNoteFindByIdDelegate implements CampaignNoteFindByIdResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignNoteFindByIdService service;
    private final CampaignNoteViewModelMapper viewModelMapper;

    @Inject
    public CampaignNoteFindByIdDelegate(CampaignNoteFindByIdService service,
                                        CampaignNoteViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public CampaignNoteViewModel findById(Long campaignId, Long noteId, Long userId) {
        log.info(() -> "Finding note %d in campaign %d for user %d".formatted(noteId, campaignId, userId));

        CampaignNote note = service.findById(noteId, userId);
        return viewModelMapper.apply(note);
    }
}

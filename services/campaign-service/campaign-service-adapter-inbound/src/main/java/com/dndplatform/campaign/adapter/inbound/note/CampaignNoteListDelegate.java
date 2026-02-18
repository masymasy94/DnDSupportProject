package com.dndplatform.campaign.adapter.inbound.note;

import com.dndplatform.campaign.adapter.inbound.note.mapper.CampaignNoteViewModelMapper;
import com.dndplatform.campaign.domain.CampaignNoteFindVisibleService;
import com.dndplatform.campaign.view.model.CampaignNoteListResource;
import com.dndplatform.campaign.view.model.vm.CampaignNoteViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignNoteListDelegate implements CampaignNoteListResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignNoteFindVisibleService service;
    private final CampaignNoteViewModelMapper viewModelMapper;

    @Inject
    public CampaignNoteListDelegate(CampaignNoteFindVisibleService service,
                                    CampaignNoteViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public List<CampaignNoteViewModel> listNotes(Long campaignId, Long userId) {
        log.info(() -> "Listing notes for campaign %d, user %d".formatted(campaignId, userId));

        return service.findVisibleNotes(campaignId, userId).stream()
                .map(viewModelMapper)
                .toList();
    }
}

package com.dndplatform.campaign.adapter.inbound.find;

import com.dndplatform.campaign.adapter.inbound.create.mapper.CampaignViewModelMapper;
import com.dndplatform.campaign.domain.CampaignFindByIdService;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.view.model.CampaignFindByIdResource;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignFindByIdDelegate implements CampaignFindByIdResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignFindByIdService service;
    private final CampaignViewModelMapper viewModelMapper;

    @Inject
    public CampaignFindByIdDelegate(CampaignFindByIdService service,
                                    CampaignViewModelMapper viewModelMapper) {
        this.service = service;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public CampaignViewModel findById(Long id) {
        log.info(() -> "Finding campaign by ID: %d".formatted(id));

        Campaign campaign = service.findById(id);
        return viewModelMapper.apply(campaign);
    }
}

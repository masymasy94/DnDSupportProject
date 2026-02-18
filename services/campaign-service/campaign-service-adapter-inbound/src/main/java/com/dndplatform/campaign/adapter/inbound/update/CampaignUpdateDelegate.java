package com.dndplatform.campaign.adapter.inbound.update;

import com.dndplatform.campaign.adapter.inbound.create.mapper.CampaignViewModelMapper;
import com.dndplatform.campaign.adapter.inbound.update.mapper.CampaignUpdateMapper;
import com.dndplatform.campaign.domain.CampaignUpdateService;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignUpdate;
import com.dndplatform.campaign.view.model.CampaignUpdateResource;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.UpdateCampaignRequest;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignUpdateDelegate implements CampaignUpdateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignUpdateService service;
    private final CampaignUpdateMapper updateMapper;
    private final CampaignViewModelMapper viewModelMapper;

    @Inject
    public CampaignUpdateDelegate(CampaignUpdateService service,
                                  CampaignUpdateMapper updateMapper,
                                  CampaignViewModelMapper viewModelMapper) {
        this.service = service;
        this.updateMapper = updateMapper;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public CampaignViewModel update(Long id, UpdateCampaignRequest request) {
        Long userId = request.userId();
        log.info(() -> "Updating campaign %d by user %d".formatted(id, userId));

        CampaignUpdate domainUpdate = updateMapper.apply(id, request);
        Campaign campaign = service.update(domainUpdate, userId);
        return viewModelMapper.apply(campaign);
    }
}

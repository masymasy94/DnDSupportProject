package com.dndplatform.campaign.adapter.inbound.create;

import com.dndplatform.campaign.adapter.inbound.create.mapper.CampaignCreateMapper;
import com.dndplatform.campaign.adapter.inbound.create.mapper.CampaignViewModelMapper;
import com.dndplatform.campaign.domain.CampaignCreateService;
import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.domain.model.CampaignCreate;
import com.dndplatform.campaign.domain.model.CampaignCreateBuilder;
import com.dndplatform.campaign.view.model.CampaignCreateResource;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CampaignCreateDelegate implements CampaignCreateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CampaignCreateService service;
    private final CampaignCreateMapper createMapper;
    private final CampaignViewModelMapper viewModelMapper;

    @Inject
    public CampaignCreateDelegate(CampaignCreateService service,
                                  CampaignCreateMapper createMapper,
                                  CampaignViewModelMapper viewModelMapper) {
        this.service = service;
        this.createMapper = createMapper;
        this.viewModelMapper = viewModelMapper;
    }

    @Override
    public CampaignViewModel create(CreateCampaignRequest request) {
        Long userId = request.userId();
        log.info(() -> "Creating campaign for user %d: %s".formatted(userId, request.name()));

        CampaignCreate baseCreate = createMapper.apply(request);
        CampaignCreate domainCreate = CampaignCreateBuilder.builder()
                .withDungeonMasterId(userId)
                .withName(baseCreate.name())
                .withDescription(baseCreate.description())
                .withMaxPlayers(baseCreate.maxPlayers())
                .withImageUrl(baseCreate.imageUrl())
                .build();

        Campaign campaign = service.create(domainCreate);
        return viewModelMapper.apply(campaign);
    }
}

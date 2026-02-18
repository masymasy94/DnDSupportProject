package com.dndplatform.campaign.adapter.inbound.create.mapper;

import com.dndplatform.campaign.domain.model.CampaignCreate;
import com.dndplatform.campaign.domain.model.CampaignCreateBuilder;
import com.dndplatform.campaign.view.model.vm.CreateCampaignRequest;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class CampaignCreateMapper implements Function<CreateCampaignRequest, CampaignCreate> {

    @Override
    public CampaignCreate apply(CreateCampaignRequest request) {
        return CampaignCreateBuilder.builder()
                .withName(request.name())
                .withDescription(request.description())
                .withMaxPlayers(request.maxPlayers() != null ? request.maxPlayers() : 6)
                .withImageUrl(request.imageUrl())
                .build();
    }
}

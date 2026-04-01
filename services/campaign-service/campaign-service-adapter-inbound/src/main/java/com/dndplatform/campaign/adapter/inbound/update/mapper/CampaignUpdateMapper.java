package com.dndplatform.campaign.adapter.inbound.update.mapper;

import com.dndplatform.campaign.domain.model.CampaignStatus;
import com.dndplatform.campaign.domain.model.CampaignUpdate;
import com.dndplatform.campaign.domain.model.CampaignUpdateBuilder;
import com.dndplatform.campaign.view.model.vm.UpdateCampaignRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CampaignUpdateMapper {

    public CampaignUpdate apply(Long id, UpdateCampaignRequest request) {
        CampaignStatus status = request.status() != null
                ? CampaignStatus.valueOf(request.status())
                : null;

        return CampaignUpdateBuilder.builder()
                .withId(id)
                .withName(request.name())
                .withDescription(request.description())
                .withStatus(status)
                .withMaxPlayers(request.maxPlayers())
                .withImageUrl(request.imageUrl())
                .build();
    }
}

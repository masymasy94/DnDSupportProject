package com.dndplatform.campaign.adapter.inbound.create.mapper;

import com.dndplatform.campaign.domain.model.Campaign;
import com.dndplatform.campaign.view.model.vm.CampaignViewModel;
import com.dndplatform.campaign.view.model.vm.CampaignViewModelBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class CampaignViewModelMapper implements Function<Campaign, CampaignViewModel> {

    @Override
    public CampaignViewModel apply(Campaign campaign) {
        return CampaignViewModelBuilder.builder()
                .withId(campaign.id())
                .withName(campaign.name())
                .withDescription(campaign.description())
                .withDungeonMasterId(campaign.dungeonMasterId())
                .withStatus(campaign.status().name())
                .withMaxPlayers(campaign.maxPlayers())
                .withImageUrl(campaign.imageUrl())
                .withCreatedAt(campaign.createdAt())
                .withUpdatedAt(campaign.updatedAt())
                .build();
    }
}

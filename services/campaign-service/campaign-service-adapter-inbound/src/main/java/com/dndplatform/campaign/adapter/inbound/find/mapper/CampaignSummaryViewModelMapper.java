package com.dndplatform.campaign.adapter.inbound.find.mapper;

import com.dndplatform.campaign.domain.model.CampaignSummary;
import com.dndplatform.campaign.view.model.vm.CampaignSummaryViewModel;
import com.dndplatform.campaign.view.model.vm.CampaignSummaryViewModelBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class CampaignSummaryViewModelMapper implements Function<CampaignSummary, CampaignSummaryViewModel> {

    @Override
    public CampaignSummaryViewModel apply(CampaignSummary summary) {
        return CampaignSummaryViewModelBuilder.builder()
                .withId(summary.id())
                .withName(summary.name())
                .withStatus(summary.status().name())
                .withPlayerCount(summary.playerCount())
                .build();
    }
}

package com.dndplatform.campaign.adapter.inbound.quest.mapper;

import com.dndplatform.campaign.domain.model.CampaignQuest;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModel;
import com.dndplatform.campaign.view.model.vm.CampaignQuestViewModelBuilder;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Function;

@ApplicationScoped
public class CampaignQuestViewModelMapper implements Function<CampaignQuest, CampaignQuestViewModel> {

    @Override
    public CampaignQuestViewModel apply(CampaignQuest quest) {
        return CampaignQuestViewModelBuilder.builder()
                .withId(quest.id())
                .withCampaignId(quest.campaignId())
                .withAuthorId(quest.authorId())
                .withTitle(quest.title())
                .withDescription(quest.description())
                .withStatus(quest.status().name())
                .withPriority(quest.priority().name())
                .withCreatedAt(quest.createdAt())
                .withUpdatedAt(quest.updatedAt())
                .build();
    }
}

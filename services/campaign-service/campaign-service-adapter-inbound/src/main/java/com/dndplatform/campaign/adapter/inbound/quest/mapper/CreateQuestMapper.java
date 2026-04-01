package com.dndplatform.campaign.adapter.inbound.quest.mapper;

import com.dndplatform.campaign.domain.model.CampaignQuestCreate;
import com.dndplatform.campaign.domain.model.CampaignQuestCreateBuilder;
import com.dndplatform.campaign.domain.model.QuestPriority;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.view.model.vm.CreateQuestRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreateQuestMapper {

    public CampaignQuestCreate apply(Long campaignId, CreateQuestRequest request, Long authorId) {
        QuestStatus status = request.status() != null
                ? QuestStatus.valueOf(request.status())
                : QuestStatus.ACTIVE;

        QuestPriority priority = request.priority() != null
                ? QuestPriority.valueOf(request.priority())
                : QuestPriority.MAIN;

        return CampaignQuestCreateBuilder.builder()
                .withCampaignId(campaignId)
                .withAuthorId(authorId)
                .withTitle(request.title())
                .withDescription(request.description())
                .withStatus(status)
                .withPriority(priority)
                .build();
    }
}

package com.dndplatform.campaign.adapter.inbound.quest.mapper;

import com.dndplatform.campaign.domain.model.CampaignQuestUpdate;
import com.dndplatform.campaign.domain.model.CampaignQuestUpdateBuilder;
import com.dndplatform.campaign.domain.model.QuestPriority;
import com.dndplatform.campaign.domain.model.QuestStatus;
import com.dndplatform.campaign.view.model.vm.UpdateQuestRequest;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UpdateQuestMapper {

    public CampaignQuestUpdate apply(Long questId, UpdateQuestRequest request) {
        QuestStatus status = request.status() != null
                ? QuestStatus.valueOf(request.status())
                : null;

        QuestPriority priority = request.priority() != null
                ? QuestPriority.valueOf(request.priority())
                : null;

        return CampaignQuestUpdateBuilder.builder()
                .withId(questId)
                .withTitle(request.title())
                .withDescription(request.description())
                .withStatus(status)
                .withPriority(priority)
                .build();
    }
}

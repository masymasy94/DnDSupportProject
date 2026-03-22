package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record CampaignQuestUpdate(
        Long id,
        String title,
        String description,
        QuestStatus status,
        QuestPriority priority
) {
}

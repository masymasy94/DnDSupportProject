package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record CampaignQuestCreate(
        Long campaignId,
        Long authorId,
        String title,
        String description,
        QuestStatus status,
        QuestPriority priority
) {
}

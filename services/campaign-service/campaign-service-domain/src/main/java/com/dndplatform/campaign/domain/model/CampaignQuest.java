package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record CampaignQuest(
        Long id,
        Long campaignId,
        Long authorId,
        String title,
        String description,
        QuestStatus status,
        QuestPriority priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

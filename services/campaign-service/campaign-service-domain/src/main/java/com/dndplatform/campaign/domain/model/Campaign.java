package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record Campaign(
        Long id,
        String name,
        String description,
        Long dungeonMasterId,
        CampaignStatus status,
        Integer maxPlayers,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

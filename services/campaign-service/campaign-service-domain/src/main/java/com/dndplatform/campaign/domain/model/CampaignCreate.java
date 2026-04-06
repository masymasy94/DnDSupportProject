package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record CampaignCreate(
        Long dungeonMasterId,
        String name,
        String description,
        Integer maxPlayers,
        String imageUrl
) {
}

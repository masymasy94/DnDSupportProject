package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record CampaignUpdate(
        Long id,
        String name,
        String description,
        CampaignStatus status,
        Integer maxPlayers,
        String imageUrl
) {
}

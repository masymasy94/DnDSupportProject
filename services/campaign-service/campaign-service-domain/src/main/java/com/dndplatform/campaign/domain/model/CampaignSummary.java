package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record CampaignSummary(
        Long id,
        String name,
        CampaignStatus status,
        int playerCount
) {
}

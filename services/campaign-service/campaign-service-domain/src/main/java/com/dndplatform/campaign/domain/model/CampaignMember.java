package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record CampaignMember(
        Long id,
        Long campaignId,
        Long userId,
        Long characterId,
        MemberRole role,
        LocalDateTime joinedAt
) {
}

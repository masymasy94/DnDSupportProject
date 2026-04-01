package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record PagedResult(
        List<CampaignSummary> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}

package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;

@Builder
public record CampaignNote(
        Long id,
        Long campaignId,
        Long authorId,
        String title,
        String content,
        NoteVisibility visibility,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

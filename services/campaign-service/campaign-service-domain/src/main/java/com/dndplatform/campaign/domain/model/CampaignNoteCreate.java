package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record CampaignNoteCreate(
        Long campaignId,
        Long authorId,
        String title,
        String content,
        NoteVisibility visibility
) {
}

package com.dndplatform.campaign.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record CampaignNoteUpdate(
        Long id,
        String title,
        String content,
        NoteVisibility visibility
) {
}

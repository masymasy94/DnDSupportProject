package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonbPropertyOrder({"id", "campaignId", "authorId", "title", "content", "visibility", "createdAt", "updatedAt"})
@Schema(description = "Campaign note details")
@Builder
public record CampaignNoteViewModel(

        @Schema(description = "Note ID", example = "1")
        Long id,

        @Schema(description = "Campaign ID", example = "1")
        Long campaignId,

        @Schema(description = "Author's user ID", example = "42")
        Long authorId,

        @Schema(description = "Note title", example = "Session 1 Recap")
        String title,

        @Schema(description = "Note content")
        String content,

        @Schema(description = "Note visibility", example = "PUBLIC")
        String visibility,

        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt
) {
}

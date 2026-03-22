package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonbPropertyOrder({"id", "campaignId", "authorId", "title", "description", "status", "priority", "createdAt", "updatedAt"})
@Schema(description = "Campaign quest details")
@Builder
public record CampaignQuestViewModel(

        @Schema(description = "Quest ID", example = "1")
        Long id,

        @Schema(description = "Campaign ID", example = "1")
        Long campaignId,

        @Schema(description = "Author's user ID", example = "42")
        Long authorId,

        @Schema(description = "Quest title", example = "Find the Lost Artifact")
        String title,

        @Schema(description = "Quest description")
        String description,

        @Schema(description = "Quest status", example = "ACTIVE")
        String status,

        @Schema(description = "Quest priority", example = "MAIN")
        String priority,

        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt
) {
}

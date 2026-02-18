package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@JsonbPropertyOrder({"id", "name", "description", "dungeonMasterId", "status", "maxPlayers", "imageUrl", "createdAt", "updatedAt"})
@Schema(description = "Full campaign details")
@Builder
public record CampaignViewModel(

        @Schema(description = "Campaign ID", example = "1")
        Long id,

        @Schema(description = "Campaign name", example = "Curse of Strahd")
        String name,

        @Schema(description = "Campaign description")
        String description,

        @Schema(description = "User ID of the Dungeon Master", example = "42")
        Long dungeonMasterId,

        @Schema(description = "Campaign status", example = "ACTIVE")
        String status,

        @Schema(description = "Maximum number of players", example = "6")
        Integer maxPlayers,

        @Schema(description = "Campaign banner/image URL")
        String imageUrl,

        @Schema(description = "Creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt
) {
}

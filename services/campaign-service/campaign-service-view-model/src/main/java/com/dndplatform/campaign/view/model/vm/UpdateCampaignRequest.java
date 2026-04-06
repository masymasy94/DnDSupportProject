package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"userId", "name", "description", "status", "maxPlayers", "imageUrl"})
@Schema(description = "Request to update a campaign")
@Builder
public record UpdateCampaignRequest(

        @Schema(description = "ID of the user updating the campaign", example = "1")
        @NotNull
        Long userId,

        @Schema(description = "Campaign name", example = "Curse of Strahd - Chapter 2")
        @Size(min = 1, max = 100)
        String name,

        @Schema(description = "Campaign description/synopsis")
        String description,

        @Schema(description = "Campaign status", example = "ACTIVE")
        String status,

        @Schema(description = "Maximum number of players", example = "6", minimum = "1", maximum = "20")
        @Min(1) @Max(20)
        Integer maxPlayers,

        @Schema(description = "Campaign banner/image URL")
        @Size(max = 255)
        String imageUrl
) {
}

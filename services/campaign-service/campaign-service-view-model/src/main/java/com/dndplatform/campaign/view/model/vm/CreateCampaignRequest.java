package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"userId", "name", "description", "maxPlayers", "imageUrl"})
@Schema(description = "Request to create a new campaign")
@Builder
public record CreateCampaignRequest(

        @Schema(description = "ID of the user creating the campaign", example = "1")
        @NotNull
        Long userId,

        @Schema(description = "Campaign name", example = "Curse of Strahd")
        @NotBlank @Size(min = 1, max = 100)
        String name,

        @Schema(description = "Campaign description/synopsis", example = "A gothic horror adventure in the land of Barovia")
        String description,

        @Schema(description = "Maximum number of players", example = "6", minimum = "1", maximum = "20")
        @Min(1) @Max(20)
        Integer maxPlayers,

        @Schema(description = "Campaign banner/image URL", example = "https://assets.example.com/campaign-banner.jpg")
        @Size(max = 255)
        String imageUrl
) {
}

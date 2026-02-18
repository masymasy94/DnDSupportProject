package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "status", "playerCount"})
@Schema(description = "Campaign summary for list view")
@Builder
public record CampaignSummaryViewModel(

        @Schema(description = "Campaign ID", example = "1")
        Long id,

        @Schema(description = "Campaign name", example = "Curse of Strahd")
        String name,

        @Schema(description = "Campaign status", example = "ACTIVE")
        String status,

        @Schema(description = "Number of players in the campaign", example = "4")
        int playerCount
) {
}

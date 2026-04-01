package com.dndplatform.campaign.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"requesterId", "userId", "characterId"})
@Schema(description = "Request to add a member to a campaign")
@Builder
public record AddMemberRequest(

        @Schema(description = "ID of the user making the request (the DM)", example = "1")
        @NotNull
        Long requesterId,

        @Schema(description = "User ID of the player to add", example = "5")
        @NotNull
        Long userId,

        @Schema(description = "Optional character ID to link", example = "12")
        Long characterId
) {
}

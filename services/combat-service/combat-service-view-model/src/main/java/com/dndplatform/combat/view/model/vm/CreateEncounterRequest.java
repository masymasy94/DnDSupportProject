package com.dndplatform.combat.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(description = "Request to create a new encounter")
@Builder
public record CreateEncounterRequest(

        @Schema(description = "Campaign ID", example = "1")
        @NotNull
        Long campaignId,

        @Schema(description = "User ID of the creator (DM)", example = "1")
        @NotNull
        Long userId,

        @Schema(description = "Encounter name", example = "Goblin Ambush")
        @NotBlank @Size(min = 1, max = 100)
        String name,

        @Schema(description = "Encounter description")
        String description,

        @Schema(description = "Party level", example = "5")
        Integer partyLevel,

        @Schema(description = "Party size", example = "4")
        Integer partySize,

        @Schema(description = "Initial participants")
        List<CreateParticipantRequest> participants
) {
}

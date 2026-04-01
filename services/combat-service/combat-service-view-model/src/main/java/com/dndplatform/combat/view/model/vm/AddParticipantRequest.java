package com.dndplatform.combat.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(description = "Request to add a participant to an encounter")
@Builder
public record AddParticipantRequest(

        @Schema(description = "User ID of the requester (DM)", example = "1")
        @NotNull
        Long userId,

        @Schema(description = "Participant name", example = "Goblin")
        @NotBlank
        String name,

        @Schema(description = "Participant type (PC or MONSTER)", example = "MONSTER")
        @NotNull
        String type,

        @Schema(description = "Maximum hit points", example = "7")
        @NotNull
        Integer maxHp,

        @Schema(description = "Armor class", example = "15")
        Integer armorClass,

        @Schema(description = "Monster ID from compendium")
        Long monsterId,

        @Schema(description = "Source JSON stat block")
        String sourceJson
) {
}

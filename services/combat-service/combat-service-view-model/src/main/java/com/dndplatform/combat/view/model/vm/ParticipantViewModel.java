package com.dndplatform.combat.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Schema(description = "Encounter participant details")
@Builder
public record ParticipantViewModel(

        @Schema(description = "Participant ID", example = "1")
        Long id,

        @Schema(description = "Encounter ID", example = "1")
        Long encounterId,

        @Schema(description = "Participant name", example = "Goblin")
        String name,

        @Schema(description = "Participant type (PC or MONSTER)", example = "MONSTER")
        String type,

        @Schema(description = "Initiative roll", example = "15")
        int initiative,

        @Schema(description = "Current hit points", example = "7")
        int currentHp,

        @Schema(description = "Maximum hit points", example = "7")
        int maxHp,

        @Schema(description = "Armor class", example = "15")
        int armorClass,

        @Schema(description = "Active conditions")
        List<String> conditions,

        @Schema(description = "Whether this participant has the current turn")
        boolean isActive,

        @Schema(description = "Sort order in initiative", example = "0")
        int sortOrder,

        @Schema(description = "Monster ID from compendium")
        Long monsterId,

        @Schema(description = "Source JSON stat block")
        String sourceJson
) {
}

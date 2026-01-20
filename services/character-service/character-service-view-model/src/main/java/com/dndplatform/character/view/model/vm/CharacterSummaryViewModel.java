package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "species", "characterClass", "level", "hitPointsCurrent", "hitPointsMax", "armorClass"})
@Schema(description = "Character summary for list view")
@Builder
public record CharacterSummaryViewModel(

        @Schema(description = "Character ID", example = "1")
        Long id,

        @Schema(description = "Character name", example = "Gandalf the Grey")
        String name,

        @Schema(description = "Character species/race", example = "Human")
        String species,

        @Schema(description = "Character class", example = "Wizard")
        String characterClass,

        @Schema(description = "Character level", example = "20")
        Integer level,

        @Schema(description = "Current hit points", example = "85")
        Integer hitPointsCurrent,

        @Schema(description = "Maximum hit points", example = "100")
        Integer hitPointsMax,

        @Schema(description = "Armor class", example = "15")
        Integer armorClass
) {
}

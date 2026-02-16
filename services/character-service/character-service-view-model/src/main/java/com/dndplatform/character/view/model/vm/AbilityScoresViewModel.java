package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"strength", "dexterity", "constitution", "intelligence", "wisdom", "charisma"})
@Schema(description = "Character ability scores with modifiers")
@Builder
public record AbilityScoresViewModel(

        @Schema(description = "Strength score", example = "14")
        Integer strength,

        @Schema(description = "Dexterity score", example = "12")
        Integer dexterity,

        @Schema(description = "Constitution score", example = "16")
        Integer constitution,

        @Schema(description = "Intelligence score", example = "10")
        Integer intelligence,

        @Schema(description = "Wisdom score", example = "16")
        Integer wisdom,

        @Schema(description = "Charisma score", example = "8")
        Integer charisma
) {
}

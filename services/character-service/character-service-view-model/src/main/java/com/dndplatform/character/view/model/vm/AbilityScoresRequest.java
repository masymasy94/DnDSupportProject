package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"strength", "dexterity", "constitution", "intelligence", "wisdom", "charisma"})
@Schema(description = "Character ability scores")
@Builder
public record AbilityScoresRequest(

        @Schema(description = "Strength score", example = "14", minimum = "1", maximum = "30")
        @NotNull @Min(1) @Max(30)
        Integer strength,

        @Schema(description = "Dexterity score", example = "12", minimum = "1", maximum = "30")
        @NotNull @Min(1) @Max(30)
        Integer dexterity,

        @Schema(description = "Constitution score", example = "16", minimum = "1", maximum = "30")
        @NotNull @Min(1) @Max(30)
        Integer constitution,

        @Schema(description = "Intelligence score", example = "10", minimum = "1", maximum = "30")
        @NotNull @Min(1) @Max(30)
        Integer intelligence,

        @Schema(description = "Wisdom score", example = "16", minimum = "1", maximum = "30")
        @NotNull @Min(1) @Max(30)
        Integer wisdom,

        @Schema(description = "Charisma score", example = "8", minimum = "1", maximum = "30")
        @NotNull @Min(1) @Max(30)
        Integer charisma
) {
}

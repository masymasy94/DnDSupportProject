package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "description", "prerequisite", "prerequisiteAbility",
        "prerequisiteLevel", "benefit", "grantsAbilityIncrease", "source", "isPublic"})
@Schema(description = "Feat information")
@Builder
public record FeatViewModel(
        @Schema(description = "Feat ID", example = "1")
        Integer id,

        @Schema(description = "Feat name", example = "Alert")
        String name,

        @Schema(description = "Full feat description")
        String description,

        @Schema(description = "Human-readable prerequisite", example = "Dexterity 13 or higher")
        String prerequisite,

        @Schema(description = "Required ability for filtering (STR, DEX, CON, INT, WIS, CHA)", example = "DEX")
        String prerequisiteAbility,

        @Schema(description = "Required character level", example = "4")
        Integer prerequisiteLevel,

        @Schema(description = "Short benefit summary", example = "+5 initiative, can't be surprised")
        String benefit,

        @Schema(description = "Whether this feat grants an ability score increase", example = "false")
        Boolean grantsAbilityIncrease,

        @Schema(description = "Source type", example = "OFFICIAL")
        String source,

        @Schema(description = "Whether homebrew is publicly visible")
        Boolean isPublic
) {}

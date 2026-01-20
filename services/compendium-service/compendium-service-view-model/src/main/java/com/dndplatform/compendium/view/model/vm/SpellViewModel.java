package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "level", "school", "castingTime", "spellRange", "components",
        "materialComponents", "duration", "concentration", "ritual", "description",
        "higherLevels", "source", "isPublic"})
@Schema(description = "Spell information")
@Builder
public record SpellViewModel(
        @Schema(description = "Spell ID", example = "1")
        Integer id,

        @Schema(description = "Spell name", example = "Fireball")
        String name,

        @Schema(description = "Spell level (0 for cantrips)", example = "3")
        Integer level,

        @Schema(description = "School of magic", example = "Evocation")
        String school,

        @Schema(description = "Casting time", example = "1 action")
        String castingTime,

        @Schema(description = "Spell range", example = "150 feet")
        String spellRange,

        @Schema(description = "Spell components (V, S, M)", example = "V, S, M")
        String components,

        @Schema(description = "Material components required")
        String materialComponents,

        @Schema(description = "Spell duration", example = "Instantaneous")
        String duration,

        @Schema(description = "Requires concentration", example = "false")
        Boolean concentration,

        @Schema(description = "Can be cast as ritual", example = "false")
        Boolean ritual,

        @Schema(description = "Spell description")
        String description,

        @Schema(description = "Effects when cast at higher levels")
        String higherLevels,

        @Schema(description = "Source type", example = "OFFICIAL")
        String source,

        @Schema(description = "Whether homebrew is publicly visible")
        Boolean isPublic
) {}

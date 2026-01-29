package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"name", "level", "school", "prepared", "source"})
@Schema(description = "Character spell")
@Builder
public record SpellViewModel(

        @Schema(description = "Spell name", example = "Cure Wounds")
        String name,

        @Schema(description = "Spell level (0 for cantrips)", example = "1")
        Integer level,

        @Schema(description = "School of magic", example = "Evocation")
        String school,

        @Schema(description = "Whether spell is prepared", example = "true")
        Boolean prepared,

        @Schema(description = "Source of the spell", example = "Class")
        String source
) {
}

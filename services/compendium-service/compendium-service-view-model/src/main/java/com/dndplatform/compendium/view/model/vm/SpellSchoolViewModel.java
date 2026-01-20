package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name"})
@Schema(description = "D&D spell school")
@Builder
public record SpellSchoolViewModel(
        @Schema(description = "Spell school ID", example = "1")
        Short id,

        @Schema(description = "Spell school name", example = "Evocation")
        String name
) {}

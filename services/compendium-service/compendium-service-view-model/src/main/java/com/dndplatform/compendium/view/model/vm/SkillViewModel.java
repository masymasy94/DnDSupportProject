package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "abilityId", "abilityName"})
@Schema(description = "D&D skill")
@Builder
public record SkillViewModel(
        @Schema(description = "Skill ID", example = "1")
        Short id,

        @Schema(description = "Skill name", example = "Acrobatics")
        String name,

        @Schema(description = "Associated ability ID", example = "2")
        Short abilityId,

        @Schema(description = "Associated ability name", example = "Dexterity")
        String abilityName
) {}

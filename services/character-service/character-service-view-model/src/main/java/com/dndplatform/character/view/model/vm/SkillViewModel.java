package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"name", "ability", "proficient", "expertise", "modifier"})
@Schema(description = "Skill proficiency with calculated modifier")
@Builder
public record SkillViewModel(

        @Schema(description = "Skill name", example = "Insight")
        String name,

        @Schema(description = "Associated ability", example = "WIS")
        String ability,

        @Schema(description = "Whether proficient in this skill", example = "true")
        Boolean proficient,

        @Schema(description = "Whether has expertise in this skill", example = "false")
        Boolean expertise,

        @Schema(description = "Total skill modifier", example = "5")
        Integer modifier
) {
}

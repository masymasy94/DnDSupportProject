package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"type", "name"})
@Schema(description = "Proficiency entry")
@Builder
public record ProficiencyRequest(

        @Schema(description = "Proficiency type", example = "ARMOR", enumeration = {"ARMOR", "WEAPON", "TOOL"})
        @NotBlank @Pattern(regexp = "ARMOR|WEAPON|TOOL")
        String type,

        @Schema(description = "Proficiency name", example = "Light Armor")
        @NotBlank
        String name
) {
}

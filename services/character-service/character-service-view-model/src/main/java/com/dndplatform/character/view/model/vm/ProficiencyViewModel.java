package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"type", "name"})
@Schema(description = "Proficiency entry")
@Builder
public record ProficiencyViewModel(

        @Schema(description = "Proficiency type", example = "ARMOR")
        String type,

        @Schema(description = "Proficiency name", example = "Light Armor")
        String name
) {
}

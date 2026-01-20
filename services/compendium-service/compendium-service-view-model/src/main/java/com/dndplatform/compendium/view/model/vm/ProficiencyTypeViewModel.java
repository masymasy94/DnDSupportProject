package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name"})
@Schema(description = "D&D proficiency type")
@Builder
public record ProficiencyTypeViewModel(
        @Schema(description = "Proficiency type ID", example = "1")
        Short id,

        @Schema(description = "Proficiency type name", example = "Armor")
        String name
) {}

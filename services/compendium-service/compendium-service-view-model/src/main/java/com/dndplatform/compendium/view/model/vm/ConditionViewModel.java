package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name"})
@Schema(description = "D&D condition")
@Builder
public record ConditionViewModel(
        @Schema(description = "Condition ID", example = "1")
        Short id,

        @Schema(description = "Condition name", example = "Blinded")
        String name
) {}

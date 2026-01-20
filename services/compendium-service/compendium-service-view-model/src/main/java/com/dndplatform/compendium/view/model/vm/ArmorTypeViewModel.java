package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name"})
@Schema(description = "D&D armor type")
@Builder
public record ArmorTypeViewModel(
        @Schema(description = "Armor type ID", example = "1")
        Short id,

        @Schema(description = "Armor type name", example = "Light")
        String name
) {}

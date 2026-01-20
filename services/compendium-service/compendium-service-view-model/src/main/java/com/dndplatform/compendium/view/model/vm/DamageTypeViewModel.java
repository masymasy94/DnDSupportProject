package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name"})
@Schema(description = "D&D damage type")
@Builder
public record DamageTypeViewModel(
        @Schema(description = "Damage type ID", example = "1")
        Short id,

        @Schema(description = "Damage type name", example = "Fire")
        String name
) {}

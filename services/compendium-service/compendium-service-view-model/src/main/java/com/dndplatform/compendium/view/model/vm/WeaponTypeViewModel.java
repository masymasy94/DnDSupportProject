package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"id", "name", "category"})
@Schema(description = "D&D weapon type")
@Builder
public record WeaponTypeViewModel(
        @Schema(description = "Weapon type ID", example = "1")
        Short id,

        @Schema(description = "Weapon type name", example = "Longsword")
        String name,

        @Schema(description = "Weapon category", example = "MARTIAL")
        String category
) {}

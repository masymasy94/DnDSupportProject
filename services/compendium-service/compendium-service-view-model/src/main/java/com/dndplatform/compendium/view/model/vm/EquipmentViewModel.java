package com.dndplatform.compendium.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@JsonbPropertyOrder({"id", "name", "category", "costGp", "costDisplay", "weightLb", "description", "source", "isPublic"})
@Schema(description = "Equipment information")
@Builder
public record EquipmentViewModel(
        @Schema(description = "Equipment ID", example = "1")
        Integer id,

        @Schema(description = "Equipment name", example = "Longsword")
        String name,

        @Schema(description = "Equipment category", example = "Martial Weapons")
        String category,

        @Schema(description = "Cost in gold pieces", example = "15.00")
        BigDecimal costGp,

        @Schema(description = "Display cost with currency", example = "15 gp")
        String costDisplay,

        @Schema(description = "Weight in pounds", example = "3.00")
        BigDecimal weightLb,

        @Schema(description = "Equipment description")
        String description,

        @Schema(description = "Source type", example = "OFFICIAL")
        String source,

        @Schema(description = "Whether homebrew is publicly visible")
        Boolean isPublic
) {}

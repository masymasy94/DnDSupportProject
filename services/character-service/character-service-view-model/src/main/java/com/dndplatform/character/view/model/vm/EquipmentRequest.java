package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"name", "quantity", "equipped"})
@Schema(description = "Equipment item")
@Builder
public record EquipmentRequest(

        @Schema(description = "Item name", example = "Longsword")
        @NotBlank
        String name,

        @Schema(description = "Quantity", example = "1", defaultValue = "1")
        @Min(1)
        Integer quantity,

        @Schema(description = "Whether item is equipped", example = "true", defaultValue = "false")
        Boolean equipped
) {
    public EquipmentRequest {
        if (quantity == null) quantity = 1;
        if (equipped == null) equipped = false;
    }
}

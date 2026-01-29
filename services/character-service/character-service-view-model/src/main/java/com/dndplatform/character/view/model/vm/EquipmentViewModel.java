package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"name", "quantity", "equipped"})
@Schema(description = "Equipment item")
@Builder
public record EquipmentViewModel(

        @Schema(description = "Item name", example = "Longsword")
        String name,

        @Schema(description = "Quantity", example = "1")
        Integer quantity,

        @Schema(description = "Whether item is equipped", example = "true")
        Boolean equipped
) {
}

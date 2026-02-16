package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"level", "total", "used"})
@Schema(description = "Spell slot allocation")
@Builder
public record SpellSlotViewModel(

        @Schema(description = "Spell level (1-9)", example = "1")
        Integer level,

        @Schema(description = "Total slots available", example = "2")
        Integer total,

        @Schema(description = "Slots used", example = "0")
        Integer used
) {
}

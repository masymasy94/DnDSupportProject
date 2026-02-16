package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"age", "height", "weight", "eyes", "hair", "skin"})
@Schema(description = "Physical characteristics")
@Builder
public record PhysicalCharacteristicsViewModel(

        @Schema(description = "Age", example = "45")
        String age,

        @Schema(description = "Height", example = "4'5\"")
        String height,

        @Schema(description = "Weight", example = "150 lbs")
        String weight,

        @Schema(description = "Eye color", example = "Brown")
        String eyes,

        @Schema(description = "Hair color", example = "Black")
        String hair,

        @Schema(description = "Skin color", example = "Tan")
        String skin
) {
}

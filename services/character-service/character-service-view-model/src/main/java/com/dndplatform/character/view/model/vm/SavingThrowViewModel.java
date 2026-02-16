package com.dndplatform.character.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"ability", "proficient", "modifier"})
@Schema(description = "Saving throw proficiency with calculated modifier")
@Builder
public record SavingThrowViewModel(

        @Schema(description = "Ability name", example = "Wisdom")
        String ability,

        @Schema(description = "Whether proficient in this save", example = "true")
        Boolean proficient,

        @Schema(description = "Total saving throw modifier", example = "5")
        Integer modifier
) {
}

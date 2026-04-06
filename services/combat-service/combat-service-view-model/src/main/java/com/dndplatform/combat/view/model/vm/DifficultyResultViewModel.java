package com.dndplatform.combat.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Difficulty calculation result")
@Builder
public record DifficultyResultViewModel(

        @Schema(description = "Difficulty rating", example = "MEDIUM")
        String rating,

        @Schema(description = "Total monster XP", example = "450")
        int totalMonsterXp,

        @Schema(description = "Adjusted XP after multiplier", example = "900")
        int adjustedXp,

        @Schema(description = "Party XP thresholds by difficulty")
        Map<String, Integer> partyXpThresholds,

        @Schema(description = "Party level", example = "5")
        int partyLevel,

        @Schema(description = "Party size", example = "4")
        int partySize
) {
}

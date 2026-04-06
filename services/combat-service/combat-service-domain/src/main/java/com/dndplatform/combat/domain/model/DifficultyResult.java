package com.dndplatform.combat.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.Map;

@Builder
public record DifficultyResult(
        DifficultyRating rating,
        int totalMonsterXp,
        int adjustedXp,
        Map<DifficultyRating, Integer> partyXpThresholds,
        int partyLevel,
        int partySize
) {
}

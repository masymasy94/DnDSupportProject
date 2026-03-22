package com.dndplatform.combat.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record Encounter(
        Long id,
        Long campaignId,
        Long createdByUserId,
        String name,
        String description,
        EncounterStatus status,
        int partyLevel,
        int partySize,
        DifficultyRating difficultyRating,
        List<EncounterParticipant> participants,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

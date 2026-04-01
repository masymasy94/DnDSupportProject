package com.dndplatform.combat.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record EncounterUpdate(
        Long id,
        String name,
        String description,
        Integer partyLevel,
        Integer partySize,
        List<ParticipantCreate> participants
) {
}

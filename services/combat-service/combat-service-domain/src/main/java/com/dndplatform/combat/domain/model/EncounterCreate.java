package com.dndplatform.combat.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record EncounterCreate(
        Long campaignId,
        Long createdByUserId,
        String name,
        String description,
        int partyLevel,
        int partySize,
        List<ParticipantCreate> participants
) {
}

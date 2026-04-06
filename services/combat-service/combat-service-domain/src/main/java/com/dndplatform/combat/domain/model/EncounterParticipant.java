package com.dndplatform.combat.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record EncounterParticipant(
        Long id,
        Long encounterId,
        String name,
        ParticipantType type,
        int initiative,
        int currentHp,
        int maxHp,
        int armorClass,
        List<String> conditions,
        boolean isActive,
        int sortOrder,
        Long monsterId,
        String sourceJson
) {
}

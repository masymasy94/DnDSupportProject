package com.dndplatform.combat.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record ParticipantCreate(
        String name,
        ParticipantType type,
        int maxHp,
        int armorClass,
        Long monsterId,
        String sourceJson
) {
}

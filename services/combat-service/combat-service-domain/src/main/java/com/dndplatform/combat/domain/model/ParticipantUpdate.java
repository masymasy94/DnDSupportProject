package com.dndplatform.combat.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record ParticipantUpdate(
        Long id,
        Integer currentHp,
        List<String> conditions,
        Boolean isActive
) {
}

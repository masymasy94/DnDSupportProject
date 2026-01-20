package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record Skill(
        Short id,
        String name,
        Short abilityId,
        String abilityName
) {}

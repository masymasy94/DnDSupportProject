package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record Feat(
        Integer id,
        String name,
        String description,
        String prerequisite,
        String prerequisiteAbility,
        Integer prerequisiteLevel,
        String benefit,
        boolean grantsAbilityIncrease,
        SourceType source,
        Long ownerId,
        Long campaignId,
        boolean isPublic
) {}

package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record Spell(
        Integer id,
        String name,
        int level,
        String school,
        String castingTime,
        String spellRange,
        String components,
        String materialComponents,
        String duration,
        boolean concentration,
        boolean ritual,
        String description,
        String higherLevels,
        SourceType source,
        Long ownerId,
        Long campaignId,
        boolean isPublic
) {}

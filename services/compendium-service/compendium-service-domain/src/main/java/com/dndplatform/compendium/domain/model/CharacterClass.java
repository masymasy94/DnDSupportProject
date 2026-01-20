package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record CharacterClass(
        Integer id,
        String name,
        String hitDie,
        String description,
        SourceType source,
        Long ownerId,
        Long campaignId,
        boolean isPublic
) {}

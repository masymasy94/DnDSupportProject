package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record MagicItem(
        Integer id,
        String name,
        String rarity,
        String type,
        boolean requiresAttunement,
        String attunementRequirements,
        String description,
        String properties,
        SourceType source,
        Long ownerId,
        Long campaignId,
        boolean isPublic
) {}

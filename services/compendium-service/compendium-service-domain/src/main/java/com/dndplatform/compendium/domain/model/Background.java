package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record Background(
        Integer id,
        String name,
        String description,
        SourceType source,
        Long ownerId,
        Long campaignId,
        boolean isPublic
) {}

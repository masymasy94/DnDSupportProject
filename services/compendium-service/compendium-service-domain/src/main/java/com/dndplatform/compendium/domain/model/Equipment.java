package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.math.BigDecimal;

@Builder
public record Equipment(
        Integer id,
        String name,
        String category,
        BigDecimal costGp,
        String costDisplay,
        BigDecimal weightLb,
        String description,
        String properties,
        SourceType source,
        Long ownerId,
        Long campaignId,
        boolean isPublic
) {}

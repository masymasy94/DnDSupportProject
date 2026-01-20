package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record ArmorType(
        Short id,
        String name
) {}

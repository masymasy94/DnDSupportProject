package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record DamageType(
        Short id,
        String name
) {}

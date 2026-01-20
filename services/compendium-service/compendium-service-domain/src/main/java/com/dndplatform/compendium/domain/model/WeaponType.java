package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record WeaponType(
        Short id,
        String name,
        String category
) {}

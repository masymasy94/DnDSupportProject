package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record Alignment(
        Short id,
        String code,
        String name
) {}

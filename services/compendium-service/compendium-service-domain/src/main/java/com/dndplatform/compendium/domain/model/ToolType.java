package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record ToolType(
        Short id,
        String name,
        String category
) {}

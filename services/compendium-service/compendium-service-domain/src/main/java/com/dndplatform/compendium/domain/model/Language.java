package com.dndplatform.compendium.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record Language(
        Short id,
        String name,
        String script,
        String type
) {}

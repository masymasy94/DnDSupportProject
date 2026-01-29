package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record Equipment(
        String name,
        Integer quantity,
        Boolean equipped
) {
}

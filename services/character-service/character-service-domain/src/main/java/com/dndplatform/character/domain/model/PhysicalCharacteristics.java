package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record PhysicalCharacteristics(
        String age,
        String height,
        String weight,
        String eyes,
        String skin,
        String hair
) {
}

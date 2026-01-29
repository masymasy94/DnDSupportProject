package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record ValidatedCompendiumData(
        String speciesName,
        String className,
        String backgroundName,
        String alignmentName,
        String hitDie,
        Integer baseSpeed
) {
}

package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record CharacterSummary(
        Long id,
        String name,
        String species,
        String characterClass,
        Integer level,
        Integer hitPointsCurrent,
        Integer hitPointsMax,
        Integer armorClass
) {
}

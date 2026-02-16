package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.util.List;

@Builder
public record CharacterCreate(
        Long userId,
        String name,
        String species,
        String subrace,
        String characterClass,
        String subclass,
        String background,
        String alignment,
        Integer level,
        AbilityScores abilityScores,
        List<String> skillProficiencies,
        List<String> savingThrowProficiencies,
        List<String> languages,
        List<Proficiency> proficiencies,
        List<Equipment> equipment,
        List<String> spells,
        PhysicalCharacteristics physicalCharacteristics,
        String personalityTraits,
        String ideals,
        String bonds,
        String flaws
) {
}

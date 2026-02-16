package com.dndplatform.character.domain.model;

import com.dndplatform.common.annotations.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record Character(
        Long id,
        Long userId,
        String name,
        String species,
        String subrace,
        String characterClass,
        String subclass,
        String background,
        String alignment,
        Integer level,
        Integer experiencePoints,
        AbilityScores abilityScores,
        Integer hitPointsCurrent,
        Integer hitPointsMax,
        Integer hitPointsTemp,
        Integer armorClass,
        Integer speed,
        Integer hitDiceTotal,
        String hitDiceType,
        Integer hitDiceUsed,
        Integer proficiencyBonus,
        Boolean inspiration,
        String spellcastingAbility,
        Integer spellSaveDc,
        Integer spellAttackBonus,
        PhysicalCharacteristics physicalCharacteristics,
        List<String> languages,
        List<SkillProficiency> skills,
        List<SavingThrowProficiency> savingThrows,
        List<Proficiency> proficiencies,
        List<Equipment> equipment,
        List<CharacterSpell> spells,
        List<SpellSlotAllocation> spellSlots,
        String personalityTraits,
        String ideals,
        String bonds,
        String flaws,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.SpellSlotAllocation;

import java.util.List;

public interface CharacterCalculatorService {
    int calculateProficiencyBonus(int level);
    int calculateModifier(int abilityScore);
    int calculateMaxHp(String hitDie, int level, int constitutionModifier);
    String getSpellcastingAbility(String className);
    String getHitDie(String className);
    int getBaseSpeed(String speciesName);
    List<SpellSlotAllocation> calculateSpellSlots(String className, int level);
}

package com.dndplatform.character.domain;

public interface CharacterMaxHpCalculator {
    int calculateMaxHp(String hitDie, int level, int constitutionModifier);
}

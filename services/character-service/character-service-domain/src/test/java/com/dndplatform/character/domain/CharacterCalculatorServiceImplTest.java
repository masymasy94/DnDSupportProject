package com.dndplatform.character.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterCalculatorServiceImplTest {

    private CharacterCalculatorServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterCalculatorServiceImpl();
    }

    // --- proficiency bonus ---

    @ParameterizedTest
    @CsvSource({"1,2", "4,2", "5,3", "8,3", "9,4", "12,4", "13,5", "16,5", "17,6", "20,6"})
    void shouldCalculateProficiencyBonus(int level, int expected) {
        assertThat(sut.calculateProficiencyBonus(level)).isEqualTo(expected);
    }

    @Test
    void shouldReturnTwoForLevelBelowOne() {
        assertThat(sut.calculateProficiencyBonus(0)).isEqualTo(2);
    }

    // --- ability modifier ---

    @ParameterizedTest
    @CsvSource({"10,0", "12,1", "8,-1", "20,5", "1,-5"})
    void shouldCalculateModifier(int score, int expected) {
        assertThat(sut.calculateModifier(score)).isEqualTo(expected);
    }

    // --- max HP ---

    @Test
    void shouldCalculateMaxHpAtLevelOne() {
        // d8 at level 1 with 0 con modifier: 8 + 0 = 8
        assertThat(sut.calculateMaxHp("d8", 1, 0)).isEqualTo(8);
    }

    @Test
    void shouldCalculateMaxHpAtLevelTwo() {
        // d8: dieAverage = (8/2)+1=5; level 1: 8+2=10; level 2: 10 + 5+2=17
        assertThat(sut.calculateMaxHp("d8", 2, 2)).isEqualTo(17);
    }

    @Test
    void shouldReturnAtLeastOneForNegativeConstitution() {
        // d6 at level 1 with -10 con modifier would be negative: max(1, result)
        assertThat(sut.calculateMaxHp("d6", 1, -10)).isEqualTo(1);
    }

    @ParameterizedTest
    @CsvSource({"d6,6", "d8,8", "d10,10", "d12,12", "d4,8"})
    void shouldUseDieSizeForMaxHpCalculation(String hitDie, int expectedBaseHp) {
        assertThat(sut.calculateMaxHp(hitDie, 1, 0)).isEqualTo(expectedBaseHp);
    }

    // --- hit die ---

    @ParameterizedTest
    @CsvSource({"Barbarian,d12", "Fighter,d10", "Bard,d8", "Sorcerer,d6", "Rogue,d8"})
    void shouldReturnHitDieForClass(String className, String expected) {
        assertThat(sut.getHitDie(className)).isEqualTo(expected);
    }

    @Test
    void shouldReturnDefaultHitDieForUnknownClass() {
        assertThat(sut.getHitDie("CustomClass")).isEqualTo("d8");
    }

    // --- spellcasting ability ---

    @ParameterizedTest
    @CsvSource({"Wizard,INT", "Cleric,WIS", "Bard,CHA", "Sorcerer,CHA", "Druid,WIS"})
    void shouldReturnSpellcastingAbilityForCasters(String className, String expected) {
        assertThat(sut.getSpellcastingAbility(className)).isEqualTo(expected);
    }

    @Test
    void shouldReturnNullForNonCaster() {
        assertThat(sut.getSpellcastingAbility("Fighter")).isNull();
    }

    // --- base speed ---

    @Test
    void shouldReturnBaseSpeedForKnownSpecies() {
        assertThat(sut.getBaseSpeed("Human")).isEqualTo(30);
        assertThat(sut.getBaseSpeed("Dwarf")).isEqualTo(25);
        assertThat(sut.getBaseSpeed("Wood Elf")).isEqualTo(35);
    }

    @Test
    void shouldReturnDefaultSpeedForUnknownSpecies() {
        assertThat(sut.getBaseSpeed("Automaton")).isEqualTo(30);
    }

    // --- spell slots ---

    @Test
    void shouldReturnEmptyForNonCasterClass() {
        assertThat(sut.calculateSpellSlots("Fighter", 5)).isEmpty();
    }

    @Test
    void shouldReturnEmptyForInvalidLevel() {
        assertThat(sut.calculateSpellSlots("Wizard", 0)).isEmpty();
        assertThat(sut.calculateSpellSlots("Wizard", 21)).isEmpty();
    }

    @Test
    void shouldReturnSpellSlotsForWizardLevel1() {
        var slots = sut.calculateSpellSlots("Wizard", 1);
        assertThat(slots).hasSize(1);
        assertThat(slots.get(0).spellLevel()).isEqualTo(1);
        assertThat(slots.get(0).slotsTotal()).isEqualTo(2);
    }

    @Test
    void shouldReturnWarlockPactMagicSlots() {
        var slots = sut.calculateSpellSlots("Warlock", 3);
        assertThat(slots).hasSize(1);
        assertThat(slots.get(0).spellLevel()).isEqualTo(2);
        assertThat(slots.get(0).slotsTotal()).isEqualTo(2);
    }

    @Test
    void shouldReturnHalfCasterSlotsForPaladin() {
        // Paladin level 2: 2 first-level slots
        var slots = sut.calculateSpellSlots("Paladin", 2);
        assertThat(slots).hasSize(1);
        assertThat(slots.get(0).spellLevel()).isEqualTo(1);
        assertThat(slots.get(0).slotsTotal()).isEqualTo(2);
    }
}

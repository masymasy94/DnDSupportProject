package com.dndplatform.character.adapter.inbound.create.generator;

import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.domain.model.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CharacterFieldMapperTest {

    private CharacterFieldMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterFieldMapper();
    }

    @Test
    void shouldMapCharacterName() {
        Character character = CharacterBuilder.builder().withName("Gandalf").build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("CharacterName", "Gandalf");
        assertThat(result.textFields()).containsEntry("CharacterName 2", "Gandalf");
    }

    @Test
    void shouldMapClassAndLevel() {
        Character character = CharacterBuilder.builder()
                .withCharacterClass("Wizard")
                .withLevel(5)
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("ClassLevel", "Wizard 5");
    }

    @Test
    void shouldMapBasicInfo() {
        Character character = CharacterBuilder.builder()
                .withSpecies("Elf")
                .withBackground("Sage")
                .withAlignment("Neutral Good")
                .withExperiencePoints(6500)
                .withProficiencyBonus(3)
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Race", "Elf");
        assertThat(result.textFields()).containsEntry("Background", "Sage");
        assertThat(result.textFields()).containsEntry("Alignment", "Neutral Good");
        assertThat(result.textFields()).containsEntry("XP", "6500");
        assertThat(result.textFields()).containsEntry("ProfBonus", "+3");
    }

    @Test
    void shouldMapAbilityScores() {
        Character character = CharacterBuilder.builder()
                .withAbilityScores(AbilityScoresBuilder.builder()
                        .withStrength(16).withDexterity(14).withConstitution(12)
                        .withIntelligence(18).withWisdom(10).withCharisma(8)
                        .build())
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("STR", "16");
        assertThat(result.textFields()).containsEntry("DEX", "14");
        assertThat(result.textFields()).containsEntry("INT", "18");
        assertThat(result.textFields()).containsEntry("CHA", "8");
    }

    @Test
    void shouldMapAbilityModifiers() {
        Character character = CharacterBuilder.builder()
                .withAbilityScores(AbilityScoresBuilder.builder()
                        .withStrength(16).withDexterity(14).withConstitution(12)
                        .withIntelligence(18).withWisdom(10).withCharisma(8)
                        .build())
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("STRmod", "+3");
        assertThat(result.textFields()).containsEntry("DEXmod", "+2");
        assertThat(result.textFields()).containsEntry("CONmod", "+1");
        assertThat(result.textFields()).containsEntry("INTmod", "+4");
        assertThat(result.textFields()).containsEntry("WISmod", "+0");
        assertThat(result.textFields()).containsEntry("CHamod", "-1");
    }

    @Test
    void shouldMapSavingThrowProficiencies() {
        Character character = CharacterBuilder.builder()
                .withSavingThrows(List.of(
                        SavingThrowProficiencyBuilder.builder().withAbility("INT").withProficient(true).withModifier(6).build(),
                        SavingThrowProficiencyBuilder.builder().withAbility("WIS").withProficient(false).withModifier(0).build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("ST Intelligence", "+6");
        assertThat(result.textFields()).containsEntry("ST Wisdom", "+0");
        assertThat(result.checkboxFields()).containsEntry("Check Box 14", true);
        assertThat(result.checkboxFields()).doesNotContainKey("Check Box 15");
    }

    @Test
    void shouldMapSkillProficiencies() {
        Character character = CharacterBuilder.builder()
                .withSkills(List.of(
                        SkillProficiencyBuilder.builder().withName("Arcana").withProficient(true).withModifier(7).build(),
                        SkillProficiencyBuilder.builder().withName("Stealth").withProficient(false).withModifier(2).build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Arcana", "+7");
        assertThat(result.textFields()).containsEntry("Stealth", "+2");
        assertThat(result.checkboxFields()).containsEntry("Check Box 25", true);
        assertThat(result.checkboxFields()).doesNotContainKey("Check Box 39");
    }

    @Test
    void shouldMapCombatStats() {
        Character character = CharacterBuilder.builder()
                .withArmorClass(15)
                .withSpeed(30)
                .withHitPointsMax(45)
                .withHitPointsCurrent(38)
                .withHitPointsTemp(5)
                .withHitDiceTotal(5)
                .withHitDiceType("d10")
                .withAbilityScores(AbilityScoresBuilder.builder()
                        .withStrength(10).withDexterity(14).withConstitution(10)
                        .withIntelligence(10).withWisdom(10).withCharisma(10)
                        .build())
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("AC", "15");
        assertThat(result.textFields()).containsEntry("Speed", "30");
        assertThat(result.textFields()).containsEntry("HPMax", "45");
        assertThat(result.textFields()).containsEntry("HPCurrent", "38");
        assertThat(result.textFields()).containsEntry("HPTemp", "5");
        assertThat(result.textFields()).containsEntry("HDTotal", "5");
        assertThat(result.textFields()).containsEntry("HD", "d10");
        assertThat(result.textFields()).containsEntry("Initiative", "+2");
    }

    @Test
    void shouldMapPassivePerception() {
        Character character = CharacterBuilder.builder()
                .withSkills(List.of(
                        SkillProficiencyBuilder.builder().withName("Perception").withProficient(true).withModifier(5).build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Passive", "15");
    }

    @Test
    void shouldDefaultPassivePerceptionTo10WhenNoSkills() {
        Character character = CharacterBuilder.builder().build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Passive", "10");
    }

    @Test
    void shouldMapSpellcasting() {
        Character character = CharacterBuilder.builder()
                .withSpellcastingAbility("Intelligence")
                .withCharacterClass("Wizard")
                .withSpellSaveDc(15)
                .withSpellAttackBonus(7)
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("SpellcastingAbility 2", "Intelligence");
        assertThat(result.textFields()).containsEntry("Spellcasting Class 2", "Wizard");
        assertThat(result.textFields()).containsEntry("SpellSaveDC  2", "15");
        assertThat(result.textFields()).containsEntry("SpellAtkBonus 2", "+7");
    }

    @Test
    void shouldMapPersonality() {
        Character character = CharacterBuilder.builder()
                .withPersonalityTraits("Curious")
                .withIdeals("Knowledge")
                .withBonds("My library")
                .withFlaws("Arrogant")
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("PersonalityTraits", "Curious");
        assertThat(result.textFields()).containsEntry("Ideals", "Knowledge");
        assertThat(result.textFields()).containsEntry("Bonds", "My library");
        assertThat(result.textFields()).containsEntry("Flaws", "Arrogant");
    }

    @Test
    void shouldMapPhysicalCharacteristics() {
        Character character = CharacterBuilder.builder()
                .withPhysicalCharacteristics(PhysicalCharacteristicsBuilder.builder()
                        .withAge("150").withHeight("6'0\"").withWeight("180 lbs")
                        .withEyes("Blue").withSkin("Fair").withHair("Silver")
                        .build())
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Age", "150");
        assertThat(result.textFields()).containsEntry("Height", "6'0\"");
        assertThat(result.textFields()).containsEntry("Eyes", "Blue");
        assertThat(result.textFields()).containsEntry("Hair", "Silver");
    }

    @Test
    void shouldMapEquipment() {
        Character character = CharacterBuilder.builder()
                .withEquipment(List.of(
                        EquipmentBuilder.builder().withName("Longsword").withQuantity(1).build(),
                        EquipmentBuilder.builder().withName("Healing Potion").withQuantity(3).build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("Equipment", "Longsword\nHealing Potion (x3)");
    }

    @Test
    void shouldMapSpellSlots() {
        Character character = CharacterBuilder.builder()
                .withSpellSlots(List.of(
                        SpellSlotAllocationBuilder.builder().withSpellLevel(1).withSlotsTotal(4).build(),
                        SpellSlotAllocationBuilder.builder().withSpellLevel(2).withSlotsTotal(3).build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("SlotsTotal 19", "4");
        assertThat(result.textFields()).containsEntry("SlotsRemaining 19", "4");
        assertThat(result.textFields()).containsEntry("SlotsTotal 20", "3");
        assertThat(result.textFields()).containsEntry("SlotsRemaining 20", "3");
    }

    @Test
    void shouldMapLanguagesAndProficiencies() {
        Character character = CharacterBuilder.builder()
                .withLanguages(List.of("Common", "Elvish"))
                .withProficiencies(List.of(
                        ProficiencyBuilder.builder().withType("Armor").withName("Light Armor").build(),
                        ProficiencyBuilder.builder().withType("Armor").withName("Medium Armor").build(),
                        ProficiencyBuilder.builder().withType("Weapon").withName("Simple Weapons").build()
                ))
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        String profLang = result.textFields().get("ProficienciesLang");
        assertThat(profLang).contains("Languages: Common, Elvish");
        assertThat(profLang).contains("Armor: Light Armor, Medium Armor");
        assertThat(profLang).contains("Weapon: Simple Weapons");
    }

    @Test
    void shouldHandleNullAbilityScores() {
        Character character = CharacterBuilder.builder().build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).doesNotContainKey("STR");
        assertThat(result.textFields()).doesNotContainKey("STRmod");
    }

    @Test
    void shouldHandleNullLists() {
        Character character = CharacterBuilder.builder()
                .withSkills(null)
                .withSavingThrows(null)
                .withEquipment(null)
                .withSpells(null)
                .withSpellSlots(null)
                .withLanguages(null)
                .withProficiencies(null)
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).isNotNull();
        assertThat(result.checkboxFields()).isNotNull();
    }

    @Test
    void shouldFormatNegativeModifier() {
        Character character = CharacterBuilder.builder()
                .withAbilityScores(AbilityScoresBuilder.builder()
                        .withStrength(8).withDexterity(10).withConstitution(10)
                        .withIntelligence(10).withWisdom(10).withCharisma(10)
                        .build())
                .build();

        CharacterFieldMap result = sut.mapFields(character);

        assertThat(result.textFields()).containsEntry("STRmod", "-1");
    }
}

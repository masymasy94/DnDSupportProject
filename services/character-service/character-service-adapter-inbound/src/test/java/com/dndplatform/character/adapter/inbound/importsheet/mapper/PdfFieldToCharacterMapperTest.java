package com.dndplatform.character.adapter.inbound.importsheet.mapper;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PdfFieldToCharacterMapperTest {

    private final PdfFieldToCharacterMapper sut = new PdfFieldToCharacterMapper();

    // -------------------------------------------------------------------------
    // Basic field mapping
    // -------------------------------------------------------------------------

    @Test
    void shouldMapBasicCharacterFields() {
        var fields = new HashMap<String, String>();
        fields.put("CharacterName", "Gandalf");
        fields.put("Race", "Human");
        fields.put("ClassLevel", "Wizard 5");
        fields.put("Background", "Sage");
        fields.put("Alignment", "Neutral Good");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.userId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Gandalf");
        assertThat(result.species()).isEqualTo("Human");
        assertThat(result.characterClass()).isEqualTo("Wizard");
        assertThat(result.level()).isEqualTo(5);
        assertThat(result.background()).isEqualTo("Sage");
        assertThat(result.alignment()).isEqualTo("Neutral Good");
    }

    @Test
    void shouldPassThroughUserId() {
        var result = sut.mapToCharacterCreate(new HashMap<>(), 99L);

        assertThat(result.userId()).isEqualTo(99L);
    }

    // -------------------------------------------------------------------------
    // Class / level parsing
    // -------------------------------------------------------------------------

    @Test
    void shouldParseClassAndLevelFromClassLevelField() {
        var fields = Map.of("ClassLevel", "Barbarian 3");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.characterClass()).isEqualTo("Barbarian");
        assertThat(result.level()).isEqualTo(3);
    }

    @Test
    void shouldDefaultToLevel1WhenClassHasNoNumber() {
        var fields = Map.of("ClassLevel", "Rogue");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.characterClass()).isEqualTo("Rogue");
        assertThat(result.level()).isEqualTo(1);
    }

    @Test
    void shouldDefaultToEmptyClassAndLevel1WhenClassLevelIsBlank() {
        var fields = Map.of("ClassLevel", "   ");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.characterClass()).isEqualTo("");
        assertThat(result.level()).isEqualTo(1);
    }

    @Test
    void shouldDefaultToEmptyClassAndLevel1WhenClassLevelIsAbsent() {
        var result = sut.mapToCharacterCreate(new HashMap<>(), 1L);

        assertThat(result.characterClass()).isEqualTo("");
        assertThat(result.level()).isEqualTo(1);
    }

    @Test
    void shouldHandleMultiWordClassName() {
        var fields = Map.of("ClassLevel", "Eldritch Knight 10");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.characterClass()).isEqualTo("Eldritch Knight");
        assertThat(result.level()).isEqualTo(10);
    }

    // -------------------------------------------------------------------------
    // Skill proficiency checkboxes
    // -------------------------------------------------------------------------

    @Test
    void shouldAddAcrobaticsWhenCheckBox23IsYes() {
        var fields = new HashMap<String, String>();
        fields.put("Check Box 23", "Yes");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.skillProficiencies()).contains("Acrobatics");
    }

    @Test
    void shouldAddMultipleSkillsWhenMultipleCheckboxesAreChecked() {
        var fields = new HashMap<String, String>();
        fields.put("Check Box 23", "Yes");   // Acrobatics
        fields.put("Check Box 26", "On");    // Athletics
        fields.put("Check Box 34", "1");     // Perception

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.skillProficiencies()).containsExactlyInAnyOrder("Acrobatics", "Athletics", "Perception");
    }

    @Test
    void shouldReturnEmptySkillsWhenNoCheckboxesAreChecked() {
        var result = sut.mapToCharacterCreate(new HashMap<>(), 1L);

        assertThat(result.skillProficiencies()).isEmpty();
    }

    @Test
    void shouldIgnoreSkillCheckboxWithUnrecognisedValue() {
        var fields = Map.of("Check Box 23", "checked");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.skillProficiencies()).doesNotContain("Acrobatics");
    }

    // -------------------------------------------------------------------------
    // Saving throw proficiency checkboxes
    // -------------------------------------------------------------------------

    @Test
    void shouldAddStrSavingThrowWhenCheckBox11IsChecked() {
        var fields = new HashMap<String, String>();
        fields.put("Check Box 11", "On");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.savingThrowProficiencies()).contains("STR");
    }

    @Test
    void shouldAddAllSavingThrowsWhenAllCheckboxesAreChecked() {
        var fields = new HashMap<String, String>();
        fields.put("Check Box 11", "Yes");  // STR
        fields.put("Check Box 12", "Yes");  // DEX
        fields.put("Check Box 13", "1");    // CON
        fields.put("Check Box 14", "On");   // INT
        fields.put("Check Box 15", "Yes");  // WIS
        fields.put("Check Box 16", "Yes");  // CHA

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.savingThrowProficiencies())
                .containsExactlyInAnyOrder("STR", "DEX", "CON", "INT", "WIS", "CHA");
    }

    @Test
    void shouldReturnEmptySavingThrowsWhenNoneChecked() {
        var result = sut.mapToCharacterCreate(new HashMap<>(), 1L);

        assertThat(result.savingThrowProficiencies()).isEmpty();
    }

    // -------------------------------------------------------------------------
    // Equipment parsing
    // -------------------------------------------------------------------------

    @Test
    void shouldParseEquipmentItemsFromNewlineSeparatedText() {
        var fields = new HashMap<String, String>();
        fields.put("Equipment", "Sword\nShield");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.equipment()).hasSize(2);
        assertThat(result.equipment().get(0).name()).isEqualTo("Sword");
        assertThat(result.equipment().get(0).quantity()).isEqualTo(1);
        assertThat(result.equipment().get(0).equipped()).isFalse();
        assertThat(result.equipment().get(1).name()).isEqualTo("Shield");
    }

    @Test
    void shouldSkipBlankLinesInEquipment() {
        var fields = new HashMap<String, String>();
        fields.put("Equipment", "Sword\n\nShield\n");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.equipment()).hasSize(2);
    }

    @Test
    void shouldReturnEmptyEquipmentWhenFieldIsAbsent() {
        var result = sut.mapToCharacterCreate(new HashMap<>(), 1L);

        assertThat(result.equipment()).isEmpty();
    }

    @Test
    void shouldReturnEmptyEquipmentWhenFieldIsBlank() {
        var fields = Map.of("Equipment", "   ");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.equipment()).isEmpty();
    }

    // -------------------------------------------------------------------------
    // Physical characteristics
    // -------------------------------------------------------------------------

    @Test
    void shouldMapPhysicalCharacteristics() {
        var fields = new HashMap<String, String>();
        fields.put("Age", "25");
        fields.put("Height", "5'10\"");
        fields.put("Weight", "160 lbs");
        fields.put("Eyes", "Blue");
        fields.put("Skin", "Fair");
        fields.put("Hair", "Grey");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.physicalCharacteristics().age()).isEqualTo("25");
        assertThat(result.physicalCharacteristics().height()).isEqualTo("5'10\"");
        assertThat(result.physicalCharacteristics().weight()).isEqualTo("160 lbs");
        assertThat(result.physicalCharacteristics().eyes()).isEqualTo("Blue");
        assertThat(result.physicalCharacteristics().skin()).isEqualTo("Fair");
        assertThat(result.physicalCharacteristics().hair()).isEqualTo("Grey");
    }

    @Test
    void shouldReturnNullPhysicalCharacteristicsFieldsWhenAbsent() {
        var result = sut.mapToCharacterCreate(new HashMap<>(), 1L);

        assertThat(result.physicalCharacteristics().age()).isNull();
        assertThat(result.physicalCharacteristics().height()).isNull();
        assertThat(result.physicalCharacteristics().weight()).isNull();
    }

    // -------------------------------------------------------------------------
    // Ability scores
    // -------------------------------------------------------------------------

    @Test
    void shouldMapAbilityScoresFromPrimaryFields() {
        var fields = new HashMap<String, String>();
        fields.put("STR", "18");
        fields.put("DEX", "14");
        fields.put("CON", "16");
        fields.put("INT", "12");
        fields.put("WIS", "10");
        fields.put("CHA", "8");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.abilityScores().strength()).isEqualTo(18);
        assertThat(result.abilityScores().dexterity()).isEqualTo(14);
        assertThat(result.abilityScores().constitution()).isEqualTo(16);
        assertThat(result.abilityScores().intelligence()).isEqualTo(12);
        assertThat(result.abilityScores().wisdom()).isEqualTo(10);
        assertThat(result.abilityScores().charisma()).isEqualTo(8);
    }

    @Test
    void shouldFallBackToAlternateAbilityScoreFields() {
        var fields = new HashMap<String, String>();
        fields.put("STRscore", "20");
        fields.put("DEXscore", "15");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.abilityScores().strength()).isEqualTo(20);
        assertThat(result.abilityScores().dexterity()).isEqualTo(15);
    }

    @Test
    void shouldDefaultAbilityScoreTo10WhenAbsent() {
        var result = sut.mapToCharacterCreate(new HashMap<>(), 1L);

        assertThat(result.abilityScores().strength()).isEqualTo(10);
        assertThat(result.abilityScores().dexterity()).isEqualTo(10);
        assertThat(result.abilityScores().constitution()).isEqualTo(10);
        assertThat(result.abilityScores().intelligence()).isEqualTo(10);
        assertThat(result.abilityScores().wisdom()).isEqualTo(10);
        assertThat(result.abilityScores().charisma()).isEqualTo(10);
    }

    // -------------------------------------------------------------------------
    // Personality / traits fields
    // -------------------------------------------------------------------------

    @Test
    void shouldMapPersonalityFields() {
        var fields = new HashMap<String, String>();
        fields.put("PersonalityTraits", "Thoughtful");
        fields.put("Ideals", "Freedom");
        fields.put("Bonds", "My staff");
        fields.put("Flaws", "Overconfident");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.personalityTraits()).isEqualTo("Thoughtful");
        assertThat(result.ideals()).isEqualTo("Freedom");
        assertThat(result.bonds()).isEqualTo("My staff");
        assertThat(result.flaws()).isEqualTo("Overconfident");
    }

    // -------------------------------------------------------------------------
    // Blank / null field handling (getField returns null)
    // -------------------------------------------------------------------------

    @Test
    void shouldReturnNullForAbsentField() {
        var result = sut.mapToCharacterCreate(new HashMap<>(), 1L);

        assertThat(result.name()).isNull();
        assertThat(result.species()).isNull();
        assertThat(result.background()).isNull();
        assertThat(result.alignment()).isNull();
        assertThat(result.personalityTraits()).isNull();
        assertThat(result.ideals()).isNull();
        assertThat(result.bonds()).isNull();
        assertThat(result.flaws()).isNull();
    }

    @Test
    void shouldReturnNullForBlankFieldValue() {
        var fields = Map.of("CharacterName", "   ");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.name()).isNull();
    }

    // -------------------------------------------------------------------------
    // Spells
    // -------------------------------------------------------------------------

    @Test
    void shouldCollectSpellsFromFieldsStartingWithSpells() {
        var fields = new HashMap<String, String>();
        fields.put("Spells 1014", "Fireball");
        fields.put("Spells 1015", "Magic Missile");
        fields.put("ClassLevel", "Wizard 5");

        var result = sut.mapToCharacterCreate(fields, 1L);

        assertThat(result.spells()).containsExactlyInAnyOrder("Fireball", "Magic Missile");
    }

    @Test
    void shouldReturnEmptySpellsWhenNoSpellFieldsPresent() {
        var result = sut.mapToCharacterCreate(new HashMap<>(), 1L);

        assertThat(result.spells()).isEmpty();
    }
}

package com.dndplatform.character.adapter.outbound.jpa.mapper;

import com.dndplatform.character.adapter.outbound.jpa.entity.*;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterEntityMapperTest {

    private CharacterEntityMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterEntityMapper();
    }

    // ========================
    // Helpers
    // ========================

    private CharacterEntity baseEntity() {
        CharacterEntity entity = new CharacterEntity();
        entity.id = 1L;
        entity.userId = 42L;
        entity.name = "Gandalf";
        entity.speciesName = "Human";
        entity.subraceName = null;
        entity.className = "Wizard";
        entity.subclassName = null;
        entity.backgroundName = "Sage";
        entity.alignmentName = "Neutral Good";
        entity.level = 10;
        entity.experiencePoints = 64000;
        entity.strength = 10;
        entity.dexterity = 14;
        entity.constitution = 12;
        entity.intelligence = 20;
        entity.wisdom = 16;
        entity.charisma = 14;
        entity.hitPointsCurrent = 58;
        entity.hitPointsMax = 65;
        entity.hitPointsTemp = 0;
        entity.armorClass = 15;
        entity.speed = 30;
        entity.hitDiceTotal = 10;
        entity.hitDiceType = "d6";
        entity.hitDiceUsed = 2;
        entity.proficiencyBonus = 4;
        entity.inspiration = true;
        entity.spellcastingAbility = "INT";
        entity.spellSaveDc = 17;
        entity.spellAttackBonus = 9;
        entity.personalityTraits = "I am always polite.";
        entity.ideals = "Knowledge is power.";
        entity.bonds = "My staff is my life.";
        entity.flaws = "I am overconfident.";
        entity.createdAt = LocalDateTime.of(2025, 1, 1, 12, 0);
        entity.updatedAt = LocalDateTime.of(2025, 6, 1, 12, 0);
        entity.age = null;
        entity.height = null;
        entity.weight = null;
        entity.eyes = null;
        entity.skin = null;
        entity.hair = null;
        return entity;
    }

    private AbilityEntity ability(String code) {
        AbilityEntity a = new AbilityEntity();
        a.code = code;
        a.name = code;
        return a;
    }

    private LanguageEntity language(String name) {
        LanguageEntity l = new LanguageEntity();
        l.name = name;
        return l;
    }

    private SkillEntity skill(String name, String abilityCode) {
        SkillEntity s = new SkillEntity();
        s.name = name;
        s.ability = ability(abilityCode);
        return s;
    }

    private SpellSchoolEntity school(String name) {
        SpellSchoolEntity ss = new SpellSchoolEntity();
        ss.name = name;
        return ss;
    }

    private SpellEntity spell(String name, short level, String schoolName) {
        SpellEntity s = new SpellEntity();
        s.name = name;
        s.level = level;
        s.school = school(schoolName);
        return s;
    }

    private ProficiencyTypeEntity profType(String name) {
        ProficiencyTypeEntity pt = new ProficiencyTypeEntity();
        pt.name = name;
        return pt;
    }

    // ========================
    // Core field mapping
    // ========================

    @Test
    void toCharacter_shouldMapAllCoreFields() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.userId()).isEqualTo(42L);
        assertThat(result.name()).isEqualTo("Gandalf");
        assertThat(result.species()).isEqualTo("Human");
        assertThat(result.subrace()).isNull();
        assertThat(result.characterClass()).isEqualTo("Wizard");
        assertThat(result.subclass()).isNull();
        assertThat(result.background()).isEqualTo("Sage");
        assertThat(result.alignment()).isEqualTo("Neutral Good");
        assertThat(result.level()).isEqualTo(10);
        assertThat(result.experiencePoints()).isEqualTo(64000);
    }

    @Test
    void toCharacter_shouldMapCombatStats() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.hitPointsCurrent()).isEqualTo(58);
        assertThat(result.hitPointsMax()).isEqualTo(65);
        assertThat(result.hitPointsTemp()).isEqualTo(0);
        assertThat(result.armorClass()).isEqualTo(15);
        assertThat(result.speed()).isEqualTo(30);
        assertThat(result.hitDiceTotal()).isEqualTo(10);
        assertThat(result.hitDiceType()).isEqualTo("d6");
        assertThat(result.hitDiceUsed()).isEqualTo(2);
        assertThat(result.proficiencyBonus()).isEqualTo(4);
        assertThat(result.inspiration()).isTrue();
    }

    @Test
    void toCharacter_shouldMapAbilityScores() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.abilityScores().strength()).isEqualTo(10);
        assertThat(result.abilityScores().dexterity()).isEqualTo(14);
        assertThat(result.abilityScores().constitution()).isEqualTo(12);
        assertThat(result.abilityScores().intelligence()).isEqualTo(20);
        assertThat(result.abilityScores().wisdom()).isEqualTo(16);
        assertThat(result.abilityScores().charisma()).isEqualTo(14);
    }

    @Test
    void toCharacter_shouldMapSpellcastingFields() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.spellcastingAbility()).isEqualTo("INT");
        assertThat(result.spellSaveDc()).isEqualTo(17);
        assertThat(result.spellAttackBonus()).isEqualTo(9);
    }

    @Test
    void toCharacter_shouldMapNullSpellcastingFields() {
        CharacterEntity entity = baseEntity();
        entity.spellcastingAbility = null;
        entity.spellSaveDc = null;
        entity.spellAttackBonus = null;

        Character result = sut.toCharacter(entity);

        assertThat(result.spellcastingAbility()).isNull();
        assertThat(result.spellSaveDc()).isNull();
        assertThat(result.spellAttackBonus()).isNull();
    }

    @Test
    void toCharacter_shouldMapPersonalityFields() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.personalityTraits()).isEqualTo("I am always polite.");
        assertThat(result.ideals()).isEqualTo("Knowledge is power.");
        assertThat(result.bonds()).isEqualTo("My staff is my life.");
        assertThat(result.flaws()).isEqualTo("I am overconfident.");
    }

    @Test
    void toCharacter_shouldMapAuditTimestamps() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.createdAt()).isEqualTo(LocalDateTime.of(2025, 1, 1, 12, 0));
        assertThat(result.updatedAt()).isEqualTo(LocalDateTime.of(2025, 6, 1, 12, 0));
    }

    // ========================
    // PhysicalCharacteristics
    // ========================

    @Test
    void toCharacter_shouldMapPhysicalCharacteristicsWhenPresent() {
        CharacterEntity entity = baseEntity();
        entity.age = "500";
        entity.height = "5'6\"";
        entity.weight = "150 lbs";
        entity.eyes = "Blue";
        entity.skin = "Fair";
        entity.hair = "Grey";

        Character result = sut.toCharacter(entity);

        assertThat(result.physicalCharacteristics()).isNotNull();
        assertThat(result.physicalCharacteristics().age()).isEqualTo("500");
        assertThat(result.physicalCharacteristics().height()).isEqualTo("5'6\"");
        assertThat(result.physicalCharacteristics().weight()).isEqualTo("150 lbs");
        assertThat(result.physicalCharacteristics().eyes()).isEqualTo("Blue");
        assertThat(result.physicalCharacteristics().skin()).isEqualTo("Fair");
        assertThat(result.physicalCharacteristics().hair()).isEqualTo("Grey");
    }

    @Test
    void toCharacter_shouldReturnNullPhysicalCharacteristicsWhenAllFieldsNull() {
        CharacterEntity entity = baseEntity();
        // All physical fields are null in baseEntity()

        Character result = sut.toCharacter(entity);

        assertThat(result.physicalCharacteristics()).isNull();
    }

    @Test
    void toCharacter_shouldBuildPhysicalCharacteristicsWhenOnlyOneFieldSet() {
        CharacterEntity entity = baseEntity();
        entity.age = "30";
        // all other physical fields remain null

        Character result = sut.toCharacter(entity);

        assertThat(result.physicalCharacteristics()).isNotNull();
        assertThat(result.physicalCharacteristics().age()).isEqualTo("30");
        assertThat(result.physicalCharacteristics().eyes()).isNull();
    }

    // ========================
    // Languages
    // ========================

    @Test
    void toCharacter_shouldMapLanguageNames() {
        CharacterEntity entity = baseEntity();
        CharacterLanguageEntity lang1 = new CharacterLanguageEntity();
        lang1.language = language("Common");
        CharacterLanguageEntity lang2 = new CharacterLanguageEntity();
        lang2.language = language("Elvish");
        entity.languages = new ArrayList<>(List.of(lang1, lang2));

        Character result = sut.toCharacter(entity);

        assertThat(result.languages()).containsExactly("Common", "Elvish");
    }

    @Test
    void toCharacter_shouldReturnEmptyLanguagesWhenNone() {
        CharacterEntity entity = baseEntity();
        // languages is already empty ArrayList

        Character result = sut.toCharacter(entity);

        assertThat(result.languages()).isEmpty();
    }

    // ========================
    // Skills
    // ========================

    @Test
    void toCharacter_shouldMapSkillProficiencies() {
        CharacterEntity entity = baseEntity();
        CharacterSkillEntity skillEntity = new CharacterSkillEntity();
        skillEntity.skill = skill("Arcana", "INT");
        skillEntity.proficient = true;
        skillEntity.expertise = false;
        entity.skills = new ArrayList<>(List.of(skillEntity));

        Character result = sut.toCharacter(entity);

        assertThat(result.skills()).hasSize(1);
        assertThat(result.skills().get(0).name()).isEqualTo("Arcana");
        assertThat(result.skills().get(0).ability()).isEqualTo("INT");
        assertThat(result.skills().get(0).proficient()).isTrue();
        assertThat(result.skills().get(0).expertise()).isFalse();
        assertThat(result.skills().get(0).modifier()).isEqualTo(0);
    }

    @Test
    void toCharacter_shouldMapExpertiseSkill() {
        CharacterEntity entity = baseEntity();
        CharacterSkillEntity skillEntity = new CharacterSkillEntity();
        skillEntity.skill = skill("Stealth", "DEX");
        skillEntity.proficient = true;
        skillEntity.expertise = true;
        entity.skills = new ArrayList<>(List.of(skillEntity));

        Character result = sut.toCharacter(entity);

        assertThat(result.skills().get(0).expertise()).isTrue();
    }

    @Test
    void toCharacter_shouldReturnEmptySkillsWhenNone() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.skills()).isEmpty();
    }

    // ========================
    // Saving Throws
    // ========================

    @Test
    void toCharacter_shouldMapSavingThrowWithProficiency() {
        CharacterEntity entity = baseEntity();
        // INT is 20 -> modifier = (20-10)/2 = 5; proficiencyBonus = 4; total = 9
        CharacterSavingThrowEntity stEntity = new CharacterSavingThrowEntity();
        stEntity.ability = ability("INT");
        stEntity.proficient = true;
        entity.savingThrows = new ArrayList<>(List.of(stEntity));

        Character result = sut.toCharacter(entity);

        assertThat(result.savingThrows()).hasSize(1);
        assertThat(result.savingThrows().get(0).ability()).isEqualTo("INT");
        assertThat(result.savingThrows().get(0).proficient()).isTrue();
        assertThat(result.savingThrows().get(0).modifier()).isEqualTo(9);
    }

    @Test
    void toCharacter_shouldMapSavingThrowWithoutProficiency() {
        CharacterEntity entity = baseEntity();
        // STR is 10 -> modifier = 0; not proficient; total = 0
        CharacterSavingThrowEntity stEntity = new CharacterSavingThrowEntity();
        stEntity.ability = ability("STR");
        stEntity.proficient = false;
        entity.savingThrows = new ArrayList<>(List.of(stEntity));

        Character result = sut.toCharacter(entity);

        assertThat(result.savingThrows().get(0).modifier()).isEqualTo(0);
        assertThat(result.savingThrows().get(0).proficient()).isFalse();
    }

    @Test
    void toCharacter_shouldMapMultipleSavingThrows() {
        CharacterEntity entity = baseEntity();
        CharacterSavingThrowEntity str = new CharacterSavingThrowEntity();
        str.ability = ability("STR");
        str.proficient = false;
        CharacterSavingThrowEntity wis = new CharacterSavingThrowEntity();
        wis.ability = ability("WIS");
        wis.proficient = true;
        entity.savingThrows = new ArrayList<>(List.of(str, wis));

        Character result = sut.toCharacter(entity);

        assertThat(result.savingThrows()).hasSize(2);
        // WIS 16 -> modifier 3 + proficiencyBonus 4 = 7
        assertThat(result.savingThrows().get(1).modifier()).isEqualTo(7);
    }

    // ========================
    // Proficiencies
    // ========================

    @Test
    void toCharacter_shouldMapProficiencies() {
        CharacterEntity entity = baseEntity();
        CharacterProficiencyEntity prof = new CharacterProficiencyEntity();
        prof.proficiencyType = profType("WEAPON");
        prof.name = "Longsword";
        entity.proficiencies = new ArrayList<>(List.of(prof));

        Character result = sut.toCharacter(entity);

        assertThat(result.proficiencies()).hasSize(1);
        assertThat(result.proficiencies().get(0).type()).isEqualTo("WEAPON");
        assertThat(result.proficiencies().get(0).name()).isEqualTo("Longsword");
    }

    @Test
    void toCharacter_shouldReturnEmptyProficienciesWhenNone() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.proficiencies()).isEmpty();
    }

    // ========================
    // Equipment
    // ========================

    @Test
    void toCharacter_shouldMapEquipment() {
        CharacterEntity entity = baseEntity();
        CharacterEquipmentEntity equip = new CharacterEquipmentEntity();
        equip.name = "Dagger";
        equip.quantity = 2;
        equip.equipped = true;
        entity.equipment = new ArrayList<>(List.of(equip));

        Character result = sut.toCharacter(entity);

        assertThat(result.equipment()).hasSize(1);
        assertThat(result.equipment().get(0).name()).isEqualTo("Dagger");
        assertThat(result.equipment().get(0).quantity()).isEqualTo(2);
        assertThat(result.equipment().get(0).equipped()).isTrue();
    }

    @Test
    void toCharacter_shouldReturnEmptyEquipmentWhenNone() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.equipment()).isEmpty();
    }

    // ========================
    // Spells
    // ========================

    @Test
    void toCharacter_shouldMapSpells() {
        CharacterEntity entity = baseEntity();
        CharacterSpellEntity spellEntity = new CharacterSpellEntity();
        spellEntity.spell = spell("Fireball", (short) 3, "Evocation");
        spellEntity.prepared = true;
        spellEntity.source = "Class";
        entity.spells = new ArrayList<>(List.of(spellEntity));

        Character result = sut.toCharacter(entity);

        assertThat(result.spells()).hasSize(1);
        assertThat(result.spells().get(0).name()).isEqualTo("Fireball");
        assertThat(result.spells().get(0).level()).isEqualTo(3);
        assertThat(result.spells().get(0).school()).isEqualTo("Evocation");
        assertThat(result.spells().get(0).prepared()).isTrue();
        assertThat(result.spells().get(0).source()).isEqualTo("Class");
    }

    @Test
    void toCharacter_shouldMapCantrip() {
        CharacterEntity entity = baseEntity();
        CharacterSpellEntity cantripEntity = new CharacterSpellEntity();
        cantripEntity.spell = spell("Prestidigitation", (short) 0, "Transmutation");
        cantripEntity.prepared = true;
        cantripEntity.source = "Class";
        entity.spells = new ArrayList<>(List.of(cantripEntity));

        Character result = sut.toCharacter(entity);

        assertThat(result.spells().get(0).level()).isEqualTo(0);
        assertThat(result.spells().get(0).name()).isEqualTo("Prestidigitation");
    }

    @Test
    void toCharacter_shouldReturnEmptySpellsWhenNone() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.spells()).isEmpty();
    }

    // ========================
    // Spell Slots
    // ========================

    @Test
    void toCharacter_shouldMapSpellSlots() {
        CharacterEntity entity = baseEntity();
        CharacterSpellSlotEntity slot = new CharacterSpellSlotEntity();
        slot.spellLevel = (short) 1;
        slot.slotsTotal = (short) 4;
        entity.spellSlots = new ArrayList<>(List.of(slot));

        Character result = sut.toCharacter(entity);

        assertThat(result.spellSlots()).hasSize(1);
        assertThat(result.spellSlots().get(0).spellLevel()).isEqualTo(1);
        assertThat(result.spellSlots().get(0).slotsTotal()).isEqualTo(4);
    }

    @Test
    void toCharacter_shouldMapMultipleSpellSlots() {
        CharacterEntity entity = baseEntity();
        CharacterSpellSlotEntity slot1 = new CharacterSpellSlotEntity();
        slot1.spellLevel = (short) 1;
        slot1.slotsTotal = (short) 4;
        CharacterSpellSlotEntity slot2 = new CharacterSpellSlotEntity();
        slot2.spellLevel = (short) 2;
        slot2.slotsTotal = (short) 3;
        entity.spellSlots = new ArrayList<>(List.of(slot1, slot2));

        Character result = sut.toCharacter(entity);

        assertThat(result.spellSlots()).hasSize(2);
        assertThat(result.spellSlots().get(0).spellLevel()).isEqualTo(1);
        assertThat(result.spellSlots().get(1).spellLevel()).isEqualTo(2);
    }

    @Test
    void toCharacter_shouldReturnEmptySpellSlotsWhenNone() {
        CharacterEntity entity = baseEntity();

        Character result = sut.toCharacter(entity);

        assertThat(result.spellSlots()).isEmpty();
    }
}

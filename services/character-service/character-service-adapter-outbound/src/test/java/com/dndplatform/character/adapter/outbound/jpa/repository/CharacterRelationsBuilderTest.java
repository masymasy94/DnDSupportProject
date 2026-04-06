package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.*;
import com.dndplatform.character.domain.CharacterSpellSlotsCalculator;
import com.dndplatform.character.domain.model.Equipment;
import com.dndplatform.character.domain.model.Proficiency;
import com.dndplatform.character.domain.model.SpellSlotAllocation;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterRelationsBuilderTest {

    @Mock
    private LanguagePanacheRepository languageRepository;

    @Mock
    private SkillPanacheRepository skillRepository;

    @Mock
    private AbilityPanacheRepository abilityRepository;

    @Mock
    private ProficiencyTypePanacheRepository proficiencyTypeRepository;

    @Mock
    private SpellPanacheRepository spellRepository;

    @Mock
    private CharacterSpellSlotsCalculator spellSlotsCalculator;

    private CharacterRelationsBuilder sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterRelationsBuilder(languageRepository, skillRepository, abilityRepository,
                proficiencyTypeRepository, spellRepository, spellSlotsCalculator);
    }

    // ========================
    // addLanguages
    // ========================

    @Test
    void addLanguages_shouldAddLanguageWhenFound() {
        CharacterEntity character = new CharacterEntity();
        LanguageEntity lang = new LanguageEntity();
        given(languageRepository.findByName("Common")).willReturn(Optional.of(lang));

        sut.addLanguages(character, List.of("Common"));

        assertThat(character.languages).hasSize(1);
        assertThat(character.languages.get(0).language).isEqualTo(lang);
        assertThat(character.languages.get(0).source).isEqualTo("character_creation");
    }

    @Test
    void addLanguages_shouldSkipWhenLanguageNotFound() {
        CharacterEntity character = new CharacterEntity();
        given(languageRepository.findByName("Unknown")).willReturn(Optional.empty());

        sut.addLanguages(character, List.of("Unknown"));

        assertThat(character.languages).isEmpty();
    }

    @Test
    void addLanguages_shouldDoNothingForNullList() {
        CharacterEntity character = new CharacterEntity();

        sut.addLanguages(character, null);

        assertThat(character.languages).isEmpty();
        then(languageRepository).shouldHaveNoInteractions();
    }

    @Test
    void addLanguages_shouldDoNothingForEmptyList() {
        CharacterEntity character = new CharacterEntity();

        sut.addLanguages(character, List.of());

        assertThat(character.languages).isEmpty();
        then(languageRepository).shouldHaveNoInteractions();
    }

    // ========================
    // addSkills
    // ========================

    @Test
    void addSkills_shouldAddSkillWhenFound() {
        CharacterEntity character = new CharacterEntity();
        SkillEntity skill = new SkillEntity();
        given(skillRepository.findByName("Acrobatics")).willReturn(Optional.of(skill));

        sut.addSkills(character, List.of("Acrobatics"));

        assertThat(character.skills).hasSize(1);
        assertThat(character.skills.get(0).skill).isEqualTo(skill);
        assertThat(character.skills.get(0).proficient).isTrue();
        assertThat(character.skills.get(0).expertise).isFalse();
    }

    @Test
    void addSkills_shouldSkipWhenSkillNotFound() {
        CharacterEntity character = new CharacterEntity();
        given(skillRepository.findByName("Unknown")).willReturn(Optional.empty());

        sut.addSkills(character, List.of("Unknown"));

        assertThat(character.skills).isEmpty();
    }

    @Test
    void addSkills_shouldDoNothingForNullList() {
        CharacterEntity character = new CharacterEntity();

        sut.addSkills(character, null);

        assertThat(character.skills).isEmpty();
        then(skillRepository).shouldHaveNoInteractions();
    }

    @Test
    void addSkills_shouldDoNothingForEmptyList() {
        CharacterEntity character = new CharacterEntity();

        sut.addSkills(character, List.of());

        assertThat(character.skills).isEmpty();
        then(skillRepository).shouldHaveNoInteractions();
    }

    // ========================
    // addSavingThrows
    // ========================

    @Test
    void addSavingThrows_shouldAddSavingThrowWhenFound() {
        CharacterEntity character = new CharacterEntity();
        AbilityEntity ability = new AbilityEntity();
        given(abilityRepository.findByCode("STR")).willReturn(Optional.of(ability));

        sut.addSavingThrows(character, List.of("STR"));

        assertThat(character.savingThrows).hasSize(1);
        assertThat(character.savingThrows.get(0).ability).isEqualTo(ability);
        assertThat(character.savingThrows.get(0).proficient).isTrue();
    }

    @Test
    void addSavingThrows_shouldSkipWhenAbilityNotFound() {
        CharacterEntity character = new CharacterEntity();
        given(abilityRepository.findByCode("UNKNOWN")).willReturn(Optional.empty());

        sut.addSavingThrows(character, List.of("UNKNOWN"));

        assertThat(character.savingThrows).isEmpty();
    }

    @Test
    void addSavingThrows_shouldDoNothingForNullList() {
        CharacterEntity character = new CharacterEntity();

        sut.addSavingThrows(character, null);

        assertThat(character.savingThrows).isEmpty();
        then(abilityRepository).shouldHaveNoInteractions();
    }

    @Test
    void addSavingThrows_shouldDoNothingForEmptyList() {
        CharacterEntity character = new CharacterEntity();

        sut.addSavingThrows(character, List.of());

        assertThat(character.savingThrows).isEmpty();
        then(abilityRepository).shouldHaveNoInteractions();
    }

    // ========================
    // addProficiencies
    // ========================

    @Test
    void addProficiencies_shouldAddProficiencyWhenTypeFound() {
        CharacterEntity character = new CharacterEntity();
        ProficiencyTypeEntity profType = new ProficiencyTypeEntity();
        Proficiency prof = new Proficiency("WEAPON", "Longsword");
        given(proficiencyTypeRepository.findByName("WEAPON")).willReturn(Optional.of(profType));

        sut.addProficiencies(character, List.of(prof));

        assertThat(character.proficiencies).hasSize(1);
        assertThat(character.proficiencies.get(0).proficiencyType).isEqualTo(profType);
        assertThat(character.proficiencies.get(0).name).isEqualTo("Longsword");
    }

    @Test
    void addProficiencies_shouldSkipWhenProficiencyTypeNotFound() {
        CharacterEntity character = new CharacterEntity();
        Proficiency prof = new Proficiency("UNKNOWN_TYPE", "SomeItem");
        given(proficiencyTypeRepository.findByName("UNKNOWN_TYPE")).willReturn(Optional.empty());

        sut.addProficiencies(character, List.of(prof));

        assertThat(character.proficiencies).isEmpty();
    }

    @Test
    void addProficiencies_shouldDoNothingForNullList() {
        CharacterEntity character = new CharacterEntity();

        sut.addProficiencies(character, null);

        assertThat(character.proficiencies).isEmpty();
        then(proficiencyTypeRepository).shouldHaveNoInteractions();
    }

    @Test
    void addProficiencies_shouldDoNothingForEmptyList() {
        CharacterEntity character = new CharacterEntity();

        sut.addProficiencies(character, List.of());

        assertThat(character.proficiencies).isEmpty();
        then(proficiencyTypeRepository).shouldHaveNoInteractions();
    }

    // ========================
    // addEquipment
    // ========================

    @Test
    void addEquipment_shouldAddEquipmentWithAllFields() {
        CharacterEntity character = new CharacterEntity();
        Equipment equip = new Equipment("Dagger", 2, true);

        sut.addEquipment(character, List.of(equip));

        assertThat(character.equipment).hasSize(1);
        assertThat(character.equipment.get(0).name).isEqualTo("Dagger");
        assertThat(character.equipment.get(0).quantity).isEqualTo(2);
        assertThat(character.equipment.get(0).equipped).isTrue();
    }

    @Test
    void addEquipment_shouldDefaultQuantityToOneWhenNull() {
        CharacterEntity character = new CharacterEntity();
        Equipment equip = new Equipment("Torch", null, false);

        sut.addEquipment(character, List.of(equip));

        assertThat(character.equipment.get(0).quantity).isEqualTo(1);
    }

    @Test
    void addEquipment_shouldDefaultEquippedToFalseWhenNull() {
        CharacterEntity character = new CharacterEntity();
        Equipment equip = new Equipment("Rope", 1, null);

        sut.addEquipment(character, List.of(equip));

        assertThat(character.equipment.get(0).equipped).isFalse();
    }

    @Test
    void addEquipment_shouldDoNothingForNullList() {
        CharacterEntity character = new CharacterEntity();

        sut.addEquipment(character, null);

        assertThat(character.equipment).isEmpty();
    }

    @Test
    void addEquipment_shouldDoNothingForEmptyList() {
        CharacterEntity character = new CharacterEntity();

        sut.addEquipment(character, List.of());

        assertThat(character.equipment).isEmpty();
    }

    // ========================
    // addSpells
    // ========================

    @Test
    void addSpells_shouldAddSpellAndMarkCantripsAsPrepared() {
        CharacterEntity character = new CharacterEntity();
        SpellEntity cantrip = new SpellEntity();
        cantrip.level = 0;
        given(spellRepository.findByName("Fire Bolt")).willReturn(Optional.of(cantrip));

        sut.addSpells(character, List.of("Fire Bolt"), "INT");

        assertThat(character.spells).hasSize(1);
        assertThat(character.spells.get(0).spell).isEqualTo(cantrip);
        assertThat(character.spells.get(0).prepared).isTrue();
        assertThat(character.spells.get(0).source).isEqualTo("Class");
    }

    @Test
    void addSpells_shouldMarkNonCantripsAsNotPrepared() {
        CharacterEntity character = new CharacterEntity();
        SpellEntity spell = new SpellEntity();
        spell.level = 1;
        given(spellRepository.findByName("Magic Missile")).willReturn(Optional.of(spell));

        sut.addSpells(character, List.of("Magic Missile"), "INT");

        assertThat(character.spells.get(0).prepared).isFalse();
    }

    @Test
    void addSpells_shouldSkipWhenSpellNotFound() {
        CharacterEntity character = new CharacterEntity();
        given(spellRepository.findByName("Unknown Spell")).willReturn(Optional.empty());

        sut.addSpells(character, List.of("Unknown Spell"), "INT");

        assertThat(character.spells).isEmpty();
    }

    @Test
    void addSpells_shouldDoNothingForNullList() {
        CharacterEntity character = new CharacterEntity();

        sut.addSpells(character, null, "INT");

        assertThat(character.spells).isEmpty();
        then(spellRepository).shouldHaveNoInteractions();
    }

    @Test
    void addSpells_shouldDoNothingForEmptyList() {
        CharacterEntity character = new CharacterEntity();

        sut.addSpells(character, List.of(), "INT");

        assertThat(character.spells).isEmpty();
        then(spellRepository).shouldHaveNoInteractions();
    }

    @Test
    void addSpells_shouldDoNothingForNullSpellcastingAbility() {
        CharacterEntity character = new CharacterEntity();

        sut.addSpells(character, List.of("Fire Bolt"), null);

        assertThat(character.spells).isEmpty();
        then(spellRepository).shouldHaveNoInteractions();
    }

    // ========================
    // addSpellSlots
    // ========================

    @Test
    void addSpellSlots_shouldAddSpellSlotsFromCalculator() {
        CharacterEntity character = new CharacterEntity();
        SpellSlotAllocation slot = new SpellSlotAllocation(1, 2);
        given(spellSlotsCalculator.calculateSpellSlots("Wizard", 1)).willReturn(List.of(slot));

        sut.addSpellSlots(character, "Wizard", 1);

        assertThat(character.spellSlots).hasSize(1);
        assertThat(character.spellSlots.get(0).spellLevel).isEqualTo((short) 1);
        assertThat(character.spellSlots.get(0).slotsTotal).isEqualTo((short) 2);
        assertThat(character.spellSlots.get(0).slotsUsed).isEqualTo((short) 0);
    }

    @Test
    void addSpellSlots_shouldAddNoSlotsWhenCalculatorReturnsEmpty() {
        CharacterEntity character = new CharacterEntity();
        given(spellSlotsCalculator.calculateSpellSlots("Fighter", 1)).willReturn(List.of());

        sut.addSpellSlots(character, "Fighter", 1);

        assertThat(character.spellSlots).isEmpty();
    }

    @Test
    void addSpellSlots_shouldAddMultipleSlotsForHigherLevel() {
        CharacterEntity character = new CharacterEntity();
        List<SpellSlotAllocation> slots = List.of(
                new SpellSlotAllocation(1, 4),
                new SpellSlotAllocation(2, 3),
                new SpellSlotAllocation(3, 2)
        );
        given(spellSlotsCalculator.calculateSpellSlots("Wizard", 5)).willReturn(slots);

        sut.addSpellSlots(character, "Wizard", 5);

        assertThat(character.spellSlots).hasSize(3);
        assertThat(character.spellSlots.get(0).spellLevel).isEqualTo((short) 1);
        assertThat(character.spellSlots.get(1).spellLevel).isEqualTo((short) 2);
        assertThat(character.spellSlots.get(2).spellLevel).isEqualTo((short) 3);
    }
}

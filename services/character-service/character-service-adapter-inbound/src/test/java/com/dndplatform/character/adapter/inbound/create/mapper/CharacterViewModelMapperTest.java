package com.dndplatform.character.adapter.inbound.create.mapper;

import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterBuilder;
import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CharacterViewModelMapperTest {

    private CharacterViewModelMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterViewModelMapper();
    }

    @Test
    void shouldMapCharacterToViewModel(@Random Character character) {
        CharacterViewModel result = sut.apply(character);

        assertThat(result.id()).isEqualTo(character.id());
        assertThat(result.name()).isEqualTo(character.name());
        assertThat(result.species()).isEqualTo(character.species());
        assertThat(result.subrace()).isEqualTo(character.subrace());
        assertThat(result.characterClass()).isEqualTo(character.characterClass());
        assertThat(result.subclass()).isEqualTo(character.subclass());
        assertThat(result.background()).isEqualTo(character.background());
        assertThat(result.alignment()).isEqualTo(character.alignment());
        assertThat(result.level()).isEqualTo(character.level());
        assertThat(result.experiencePoints()).isEqualTo(character.experiencePoints());
        assertThat(result.hitPointsCurrent()).isEqualTo(character.hitPointsCurrent());
        assertThat(result.hitPointsMax()).isEqualTo(character.hitPointsMax());
        assertThat(result.hitPointsTemp()).isEqualTo(character.hitPointsTemp());
        assertThat(result.armorClass()).isEqualTo(character.armorClass());
        assertThat(result.speed()).isEqualTo(character.speed());
        assertThat(result.hitDiceTotal()).isEqualTo(character.hitDiceTotal());
        assertThat(result.hitDiceType()).isEqualTo(character.hitDiceType());
        assertThat(result.hitDiceUsed()).isEqualTo(character.hitDiceUsed());
        assertThat(result.proficiencyBonus()).isEqualTo(character.proficiencyBonus());
        assertThat(result.inspiration()).isEqualTo(character.inspiration());
        assertThat(result.spellcastingAbility()).isEqualTo(character.spellcastingAbility());
        assertThat(result.spellSaveDc()).isEqualTo(character.spellSaveDc());
        assertThat(result.spellAttackBonus()).isEqualTo(character.spellAttackBonus());
        assertThat(result.languages()).isEqualTo(character.languages());
        assertThat(result.personalityTraits()).isEqualTo(character.personalityTraits());
        assertThat(result.ideals()).isEqualTo(character.ideals());
        assertThat(result.bonds()).isEqualTo(character.bonds());
        assertThat(result.flaws()).isEqualTo(character.flaws());
        assertThat(result.createdAt()).isEqualTo(character.createdAt());
        assertThat(result.updatedAt()).isEqualTo(character.updatedAt());

        assertThat(result.abilityScores()).isNotNull();
        assertThat(result.abilityScores().strength()).isEqualTo(character.abilityScores().strength());
        assertThat(result.abilityScores().dexterity()).isEqualTo(character.abilityScores().dexterity());
        assertThat(result.abilityScores().constitution()).isEqualTo(character.abilityScores().constitution());
        assertThat(result.abilityScores().intelligence()).isEqualTo(character.abilityScores().intelligence());
        assertThat(result.abilityScores().wisdom()).isEqualTo(character.abilityScores().wisdom());
        assertThat(result.abilityScores().charisma()).isEqualTo(character.abilityScores().charisma());

        assertThat(result.physicalCharacteristics()).isNotNull();
        assertThat(result.physicalCharacteristics().age()).isEqualTo(character.physicalCharacteristics().age());
        assertThat(result.physicalCharacteristics().height()).isEqualTo(character.physicalCharacteristics().height());
        assertThat(result.physicalCharacteristics().weight()).isEqualTo(character.physicalCharacteristics().weight());
        assertThat(result.physicalCharacteristics().eyes()).isEqualTo(character.physicalCharacteristics().eyes());
        assertThat(result.physicalCharacteristics().skin()).isEqualTo(character.physicalCharacteristics().skin());
        assertThat(result.physicalCharacteristics().hair()).isEqualTo(character.physicalCharacteristics().hair());

        assertThat(result.skills()).isNotNull();
        assertThat(result.skills()).hasSameSizeAs(character.skills());

        assertThat(result.savingThrows()).isNotNull();
        assertThat(result.savingThrows()).hasSameSizeAs(character.savingThrows());

        assertThat(result.proficiencies()).isNotNull();
        assertThat(result.proficiencies()).hasSameSizeAs(character.proficiencies());

        assertThat(result.equipment()).isNotNull();
        assertThat(result.equipment()).hasSameSizeAs(character.equipment());

        assertThat(result.spells()).isNotNull();
        assertThat(result.spells()).hasSameSizeAs(character.spells());

        assertThat(result.spellSlots()).isNotNull();
        assertThat(result.spellSlots()).hasSameSizeAs(character.spellSlots());
        if (!result.spellSlots().isEmpty()) {
            assertThat(result.spellSlots().get(0).level()).isEqualTo(character.spellSlots().get(0).spellLevel());
            assertThat(result.spellSlots().get(0).total()).isEqualTo(character.spellSlots().get(0).slotsTotal());
            assertThat(result.spellSlots().get(0).used()).isEqualTo(0);
        }
    }

    @Test
    void shouldReturnEmptyListsWhenSubObjectsAreNull() {
        Character character = CharacterBuilder.builder()
                .withId(1L)
                .withUserId(1L)
                .withName("Gandalf")
                .withSpecies("Human")
                .withCharacterClass("Wizard")
                .withLevel(20)
                .withExperiencePoints(0)
                .withHitPointsCurrent(80)
                .withHitPointsMax(80)
                .withHitPointsTemp(0)
                .withArmorClass(12)
                .withSpeed(30)
                .withHitDiceTotal(20)
                .withHitDiceType("d6")
                .withHitDiceUsed(0)
                .withProficiencyBonus(6)
                .withInspiration(false)
                .withAbilityScores(null)
                .withPhysicalCharacteristics(null)
                .withSkills(null)
                .withSavingThrows(null)
                .withProficiencies(null)
                .withEquipment(null)
                .withSpells(null)
                .withSpellSlots(null)
                .withLanguages(null)
                .withSubrace(null)
                .withSubclass(null)
                .withBackground(null)
                .withAlignment(null)
                .withSpellcastingAbility(null)
                .withSpellSaveDc(null)
                .withSpellAttackBonus(null)
                .withPersonalityTraits(null)
                .withIdeals(null)
                .withBonds(null)
                .withFlaws(null)
                .withCreatedAt(null)
                .withUpdatedAt(null)
                .build();

        CharacterViewModel result = sut.apply(character);

        assertThat(result.abilityScores()).isNull();
        assertThat(result.physicalCharacteristics()).isNull();
        assertThat(result.skills()).isEqualTo(List.of());
        assertThat(result.savingThrows()).isEqualTo(List.of());
        assertThat(result.proficiencies()).isEqualTo(List.of());
        assertThat(result.equipment()).isEqualTo(List.of());
        assertThat(result.spells()).isEqualTo(List.of());
        assertThat(result.spellSlots()).isEqualTo(List.of());
    }
}

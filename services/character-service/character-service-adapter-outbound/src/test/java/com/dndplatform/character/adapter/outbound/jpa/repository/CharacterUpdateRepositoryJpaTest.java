package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterEntity;
import com.dndplatform.character.adapter.outbound.jpa.mapper.CharacterEntityMapper;
import com.dndplatform.character.domain.CharacterModifierCalculator;
import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterUpdateRepositoryJpaTest {

    @Mock
    private CharacterEntityMapper mapper;

    @Mock
    private CharacterModifierCalculator modifierCalculator;

    @Mock
    private CharacterPanacheRepository panacheRepository;

    @Mock
    private CharacterRelationsBuilder relationsBuilder;

    private CharacterUpdateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterUpdateRepositoryJpa(mapper, modifierCalculator, panacheRepository, relationsBuilder);
    }

    @Test
    void update_shouldThrowNotFoundExceptionWhenCharacterDoesNotExist() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = buildCompendiumData();
        given(panacheRepository.findByIdAndUserId(99L, 1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.update(99L, input, compendiumData, 2, 8, "INT", 13, 5))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99")
                .hasMessageContaining("1");
    }

    @Test
    void update_shouldReturnMappedCharacterOnSuccess() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = buildCompendiumData();
        CharacterEntity entity = buildExistingEntity();
        Character expected = buildExpectedCharacter();

        given(panacheRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(entity));
        given(modifierCalculator.calculateModifier(anyInt())).willReturn(0);
        given(mapper.toCharacter(entity)).willReturn(expected);

        Character result = sut.update(1L, input, compendiumData, 2, 8, "INT", 13, 5);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void update_shouldClearCollectionsBeforeFlushAndReAddAfter() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = buildCompendiumData();
        CharacterEntity entity = buildExistingEntity();

        given(panacheRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(entity));
        given(modifierCalculator.calculateModifier(anyInt())).willReturn(0);
        given(mapper.toCharacter(any())).willReturn(buildExpectedCharacter());

        sut.update(1L, input, compendiumData, 2, 8, "INT", 13, 5);

        // Collections should be cleared
        assertThat(entity.languages).isEmpty();
        assertThat(entity.skills).isEmpty();
        assertThat(entity.savingThrows).isEmpty();
        assertThat(entity.proficiencies).isEmpty();
        assertThat(entity.equipment).isEmpty();
        assertThat(entity.spells).isEmpty();
        assertThat(entity.spellSlots).isEmpty();
    }

    @Test
    void update_shouldCallRelationsBuilderForAllRelations() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = buildCompendiumData();
        CharacterEntity entity = buildExistingEntity();

        given(panacheRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(entity));
        given(modifierCalculator.calculateModifier(anyInt())).willReturn(0);
        given(mapper.toCharacter(any())).willReturn(buildExpectedCharacter());

        sut.update(1L, input, compendiumData, 2, 8, "INT", 13, 5);

        then(relationsBuilder).should().addLanguages(entity, input.languages());
        then(relationsBuilder).should().addSkills(entity, input.skillProficiencies());
        then(relationsBuilder).should().addSavingThrows(entity, input.savingThrowProficiencies());
        then(relationsBuilder).should().addProficiencies(entity, input.proficiencies());
        then(relationsBuilder).should().addEquipment(entity, input.equipment());
        then(relationsBuilder).should().addSpells(entity, input.spells(), "INT");
        then(relationsBuilder).should().addSpellSlots(entity, input.characterClass(), input.level());
    }

    @Test
    void update_shouldFlushTwice_onceAfterClearAndOnceAfterReAdd() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = buildCompendiumData();
        CharacterEntity entity = buildExistingEntity();

        given(panacheRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(entity));
        given(modifierCalculator.calculateModifier(anyInt())).willReturn(0);
        given(mapper.toCharacter(any())).willReturn(buildExpectedCharacter());

        sut.update(1L, input, compendiumData, 2, 8, "INT", 13, 5);

        then(panacheRepository).should(times(2)).flush();
    }

    @Test
    void update_shouldFlushAfterClearAndBeforeRelationsBuilder() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = buildCompendiumData();
        CharacterEntity entity = buildExistingEntity();

        given(panacheRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(entity));
        given(modifierCalculator.calculateModifier(anyInt())).willReturn(0);
        given(mapper.toCharacter(any())).willReturn(buildExpectedCharacter());

        sut.update(1L, input, compendiumData, 2, 8, "INT", 13, 5);

        var inOrderVerification = inOrder(panacheRepository, relationsBuilder);
        then(panacheRepository).should(inOrderVerification).flush(); // after clear
        then(relationsBuilder).should(inOrderVerification).addLanguages(any(), any());
        then(panacheRepository).should(inOrderVerification).flush(); // after re-add
    }

    @Test
    void update_shouldUpdateScalarFieldsOnEntity() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = buildCompendiumData();
        CharacterEntity entity = buildExistingEntity();
        entity.hitPointsCurrent = 5; // lower than max to test Math.min logic

        given(panacheRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(entity));
        given(modifierCalculator.calculateModifier(10)).willReturn(0);
        given(mapper.toCharacter(any())).willReturn(buildExpectedCharacter());

        sut.update(1L, input, compendiumData, 2, 8, "INT", 13, 5);

        assertThat(entity.name).isEqualTo("Gandalf");
        assertThat(entity.speciesName).isEqualTo("Human");
        assertThat(entity.className).isEqualTo("Wizard");
        assertThat(entity.backgroundName).isEqualTo("Sage");
        assertThat(entity.level).isEqualTo(1);
        assertThat(entity.hitPointsMax).isEqualTo(8);
        assertThat(entity.hitPointsCurrent).isEqualTo(5); // min(5, 8)
        assertThat(entity.armorClass).isEqualTo(10); // 10 + modifier(0)
        assertThat(entity.proficiencyBonus).isEqualTo(2);
        assertThat(entity.spellcastingAbility).isEqualTo("INT");
        assertThat(entity.spellSaveDc).isEqualTo(13);
        assertThat(entity.spellAttackBonus).isEqualTo(5);
        assertThat(entity.updatedAt).isNotNull();
    }

    @Test
    void update_shouldClearPhysicalCharacteristicsWhenInputHasNone() {
        CharacterCreate input = buildCharacterCreate(); // physicalCharacteristics is null
        ValidatedCompendiumData compendiumData = buildCompendiumData();
        CharacterEntity entity = buildExistingEntity();
        entity.age = "100";
        entity.height = "5ft";

        given(panacheRepository.findByIdAndUserId(1L, 1L)).willReturn(Optional.of(entity));
        given(modifierCalculator.calculateModifier(anyInt())).willReturn(0);
        given(mapper.toCharacter(any())).willReturn(buildExpectedCharacter());

        sut.update(1L, input, compendiumData, 2, 8, "INT", 13, 5);

        assertThat(entity.age).isNull();
        assertThat(entity.height).isNull();
        assertThat(entity.weight).isNull();
        assertThat(entity.eyes).isNull();
        assertThat(entity.skin).isNull();
        assertThat(entity.hair).isNull();
    }

    private CharacterCreate buildCharacterCreate() {
        return new CharacterCreate(
                1L,
                "Gandalf",
                "Human",
                null,
                "Wizard",
                null,
                "Sage",
                "Neutral Good",
                1,
                new AbilityScores(10, 10, 10, 10, 10, 10),
                List.of("Arcana"),
                List.of("INT", "WIS"),
                List.of("Common"),
                List.of(new Proficiency("WEAPON", "Dagger")),
                List.of(new Equipment("Staff", 1, true)),
                List.of("Fire Bolt"),
                null,
                "Wise",
                "Knowledge",
                "The Library",
                "Overconfident"
        );
    }

    private ValidatedCompendiumData buildCompendiumData() {
        return new ValidatedCompendiumData("Human", "Wizard", "Sage", "Neutral Good", "d6", 30);
    }

    private CharacterEntity buildExistingEntity() {
        CharacterEntity entity = new CharacterEntity();
        entity.hitPointsCurrent = 8;
        entity.languages = new ArrayList<>();
        entity.skills = new ArrayList<>();
        entity.savingThrows = new ArrayList<>();
        entity.proficiencies = new ArrayList<>();
        entity.equipment = new ArrayList<>();
        entity.spells = new ArrayList<>();
        entity.spellSlots = new ArrayList<>();
        return entity;
    }

    private Character buildExpectedCharacter() {
        return new Character(
                1L, 1L, "Gandalf", "Human", null, "Wizard", null, "Sage", "Neutral Good",
                1, 0, new AbilityScores(10, 10, 10, 10, 10, 10),
                8, 8, 0, 10, 30, 1, "d6", 0, 2, false, "INT", 13, 5,
                null, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                null, null, null, null, LocalDateTime.now(), LocalDateTime.now()
        );
    }
}

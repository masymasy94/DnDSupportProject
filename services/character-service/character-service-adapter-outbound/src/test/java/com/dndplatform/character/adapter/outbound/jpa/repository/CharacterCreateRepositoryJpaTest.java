package com.dndplatform.character.adapter.outbound.jpa.repository;

import com.dndplatform.character.adapter.outbound.jpa.entity.CharacterEntity;
import com.dndplatform.character.adapter.outbound.jpa.mapper.CharacterEntityMapper;
import com.dndplatform.character.domain.CharacterModifierCalculator;
import com.dndplatform.character.domain.model.*;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterCreateRepositoryJpaTest {

    @Mock
    private CharacterEntityMapper mapper;

    @Mock
    private CharacterModifierCalculator modifierCalculator;

    @Mock
    private CharacterPanacheRepository panacheRepository;

    @Mock
    private CharacterRelationsBuilder relationsBuilder;

    private CharacterCreateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterCreateRepositoryJpa(mapper, modifierCalculator, panacheRepository, relationsBuilder);
    }

    @Test
    void save_shouldPersistEntityAndReturnMappedCharacter() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = new ValidatedCompendiumData("Human", "Wizard", "Sage", "Neutral Good", "d6", 30);
        Character expected = buildExpectedCharacter();

        given(modifierCalculator.calculateModifier(anyInt())).willReturn(0);
        willDoNothing().given(panacheRepository).persist(any(CharacterEntity.class));
        willDoNothing().given(relationsBuilder).addLanguages(any(), any());
        willDoNothing().given(relationsBuilder).addSkills(any(), any());
        willDoNothing().given(relationsBuilder).addSavingThrows(any(), any());
        willDoNothing().given(relationsBuilder).addProficiencies(any(), any());
        willDoNothing().given(relationsBuilder).addEquipment(any(), any());
        willDoNothing().given(relationsBuilder).addSpells(any(), any(), any());
        willDoNothing().given(relationsBuilder).addSpellSlots(any(), any(), anyInt());
        given(mapper.toCharacter(any(CharacterEntity.class))).willReturn(expected);

        Character result = sut.save(input, compendiumData, 2, 8, "INT", 13, 5);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void save_shouldPersistBeforeCallingRelationsBuilder() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = new ValidatedCompendiumData("Human", "Wizard", "Sage", "Neutral Good", "d6", 30);

        given(modifierCalculator.calculateModifier(anyInt())).willReturn(0);
        given(mapper.toCharacter(any())).willReturn(buildExpectedCharacter());

        sut.save(input, compendiumData, 2, 8, "INT", 13, 5);

        var inOrderVerification = inOrder(panacheRepository, relationsBuilder);
        then(panacheRepository).should(inOrderVerification).persist(any(CharacterEntity.class));
        then(relationsBuilder).should(inOrderVerification).addLanguages(any(), eq(input.languages()));
        then(relationsBuilder).should(inOrderVerification).addSkills(any(), eq(input.skillProficiencies()));
        then(relationsBuilder).should(inOrderVerification).addSavingThrows(any(), eq(input.savingThrowProficiencies()));
        then(relationsBuilder).should(inOrderVerification).addProficiencies(any(), eq(input.proficiencies()));
        then(relationsBuilder).should(inOrderVerification).addEquipment(any(), eq(input.equipment()));
        then(relationsBuilder).should(inOrderVerification).addSpells(any(), eq(input.spells()), eq("INT"));
        then(relationsBuilder).should(inOrderVerification).addSpellSlots(any(), eq(input.characterClass()), eq(input.level()));
        then(panacheRepository).should(inOrderVerification).flush();
    }

    @Test
    void save_shouldPopulateEntityFieldsFromInput() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = new ValidatedCompendiumData("Human", "Wizard", "Sage", "Lawful Good", "d6", 30);

        given(modifierCalculator.calculateModifier(10)).willReturn(0);
        given(mapper.toCharacter(any())).willReturn(buildExpectedCharacter());

        sut.save(input, compendiumData, 2, 8, "INT", 13, 5);

        ArgumentCaptor<CharacterEntity> captor = ArgumentCaptor.forClass(CharacterEntity.class);
        then(panacheRepository).should().persist(captor.capture());

        CharacterEntity saved = captor.getValue();
        assertThat(saved.userId).isEqualTo(1L);
        assertThat(saved.name).isEqualTo("Gandalf");
        assertThat(saved.speciesName).isEqualTo("Human");
        assertThat(saved.className).isEqualTo("Wizard");
        assertThat(saved.backgroundName).isEqualTo("Sage");
        assertThat(saved.alignmentName).isEqualTo("Lawful Good");
        assertThat(saved.level).isEqualTo(1);
        assertThat(saved.experiencePoints).isEqualTo(0);
        assertThat(saved.hitPointsCurrent).isEqualTo(8);
        assertThat(saved.hitPointsMax).isEqualTo(8);
        assertThat(saved.hitPointsTemp).isEqualTo(0);
        assertThat(saved.armorClass).isEqualTo(10);
        assertThat(saved.speed).isEqualTo(30);
        assertThat(saved.hitDiceTotal).isEqualTo(1);
        assertThat(saved.hitDiceType).isEqualTo("d6");
        assertThat(saved.hitDiceUsed).isEqualTo(0);
        assertThat(saved.proficiencyBonus).isEqualTo(2);
        assertThat(saved.inspiration).isFalse();
        assertThat(saved.spellcastingAbility).isEqualTo("INT");
        assertThat(saved.spellSaveDc).isEqualTo(13);
        assertThat(saved.spellAttackBonus).isEqualTo(5);
        assertThat(saved.createdAt).isNotNull();
    }

    @Test
    void save_shouldFlushAfterRelationsBuilder() {
        CharacterCreate input = buildCharacterCreate();
        ValidatedCompendiumData compendiumData = new ValidatedCompendiumData("Human", "Wizard", "Sage", "Neutral Good", "d6", 30);

        given(modifierCalculator.calculateModifier(anyInt())).willReturn(0);
        given(mapper.toCharacter(any())).willReturn(buildExpectedCharacter());

        sut.save(input, compendiumData, 2, 8, "INT", 13, 5);

        then(panacheRepository).should().flush();
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
                List.of("Common", "Elvish"),
                List.of(new Proficiency("WEAPON", "Dagger")),
                List.of(new Equipment("Staff", 1, true)),
                List.of("Fire Bolt"),
                null,
                "Curious and wise",
                "Knowledge",
                "The Library",
                "Overconfident"
        );
    }

    private Character buildExpectedCharacter() {
        return new Character(
                1L, 1L, "Gandalf", "Human", null, "Wizard", null, "Sage", "Neutral Good",
                1, 0, new AbilityScores(10, 10, 10, 10, 10, 10),
                8, 8, 0, 10, 30, 1, "d6", 0, 2, false, "INT", 13, 5,
                null, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
                null, null, null, null, LocalDateTime.now(), null
        );
    }
}

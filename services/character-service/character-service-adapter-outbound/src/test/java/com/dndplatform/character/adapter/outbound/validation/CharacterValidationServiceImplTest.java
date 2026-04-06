package com.dndplatform.character.adapter.outbound.validation;

import com.dndplatform.character.domain.CharacterBaseSpeedProvider;
import com.dndplatform.character.domain.CharacterHitDieProvider;
import com.dndplatform.character.domain.CharacterValidationService;
import com.dndplatform.character.domain.model.AbilityScores;
import com.dndplatform.character.domain.model.CharacterCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith(MockitoExtension.class)
class CharacterValidationServiceImplTest {

    @Mock
    private CharacterHitDieProvider hitDieProvider;

    @Mock
    private CharacterBaseSpeedProvider baseSpeedProvider;

    private CharacterValidationServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterValidationServiceImpl(hitDieProvider, baseSpeedProvider);
    }

    @Test
    void shouldValidateCharacterWithAllFields() {
        var input = createCharacterInput("Wizard", "Human", "Soldier", "Lawful Good");

        given(hitDieProvider.getHitDie("Wizard")).willReturn("d6");
        given(baseSpeedProvider.getBaseSpeed("Human")).willReturn(30);

        var result = sut.validate(input);

        assertThat(result.speciesName()).isEqualTo("Human");
        assertThat(result.className()).isEqualTo("Wizard");
        assertThat(result.backgroundName()).isEqualTo("Soldier");
        assertThat(result.alignmentName()).isEqualTo("Lawful Good");
        assertThat(result.hitDie()).isEqualTo("d6");
        assertThat(result.baseSpeed()).isEqualTo(30);

        var inOrder = inOrder(hitDieProvider, baseSpeedProvider);
        then(hitDieProvider).should(inOrder).getHitDie("Wizard");
        then(baseSpeedProvider).should(inOrder).getBaseSpeed("Human");
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldValidateCharacterWithElfSpecies() {
        var input = createCharacterInput("Rogue", "Elf", "Entertainer", "Chaotic Good");

        given(hitDieProvider.getHitDie("Rogue")).willReturn("d8");
        given(baseSpeedProvider.getBaseSpeed("Elf")).willReturn(30);

        var result = sut.validate(input);

        assertThat(result.speciesName()).isEqualTo("Elf");
        assertThat(result.className()).isEqualTo("Rogue");
        assertThat(result.hitDie()).isEqualTo("d8");
        assertThat(result.baseSpeed()).isEqualTo(30);
    }

    @Test
    void shouldValidateCharacterWithDwarfSpecies() {
        var input = createCharacterInput("Fighter", "Dwarf", "Hermit", "Lawful Neutral");

        given(hitDieProvider.getHitDie("Fighter")).willReturn("d10");
        given(baseSpeedProvider.getBaseSpeed("Dwarf")).willReturn(25);

        var result = sut.validate(input);

        assertThat(result.speciesName()).isEqualTo("Dwarf");
        assertThat(result.className()).isEqualTo("Fighter");
        assertThat(result.hitDie()).isEqualTo("d10");
        assertThat(result.baseSpeed()).isEqualTo(25);
    }

    private CharacterCreate createCharacterInput(String characterClass, String species, String background, String alignment) {
        return new CharacterCreate(
                1L,
                "TestCharacter",
                species,
                null,
                characterClass,
                null,
                background,
                alignment,
                1,
                new AbilityScores(10, 10, 10, 10, 10, 10),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}

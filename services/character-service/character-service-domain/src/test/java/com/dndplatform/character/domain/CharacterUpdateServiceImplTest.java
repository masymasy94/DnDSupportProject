package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.AbilityScores;
import com.dndplatform.character.domain.model.AbilityScoresBuilder;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.model.CharacterCreateBuilder;
import com.dndplatform.character.domain.model.ValidatedCompendiumData;
import com.dndplatform.character.domain.repository.CharacterUpdateRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterUpdateServiceImplTest {

    @Mock private CharacterValidationService validationService;
    @Mock private CharacterProficiencyBonusCalculator proficiencyBonusCalculator;
    @Mock private CharacterModifierCalculator modifierCalculator;
    @Mock private CharacterMaxHpCalculator maxHpCalculator;
    @Mock private CharacterHitDieProvider hitDieProvider;
    @Mock private CharacterSpellcastingAbilityProvider spellcastingAbilityProvider;
    @Mock private CharacterUpdateRepository repository;

    private CharacterUpdateServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterUpdateServiceImpl(
                validationService, proficiencyBonusCalculator, modifierCalculator,
                maxHpCalculator, hitDieProvider, spellcastingAbilityProvider, repository);
    }

    @Test
    void shouldUpdateNonSpellcasterCharacter(@Random ValidatedCompendiumData compendiumData,
                                             @Random Character expected) {
        AbilityScores scores = AbilityScoresBuilder.builder()
                .withConstitution(12).withStrength(18).withDexterity(10)
                .withIntelligence(8).withWisdom(10).withCharisma(8).build();
        CharacterCreate input = CharacterCreateBuilder.builder()
                .withName("Conan").withCharacterClass("Barbarian").withLevel(3)
                .withAbilityScores(scores).build();

        given(validationService.validate(input)).willReturn(compendiumData);
        given(proficiencyBonusCalculator.calculateProficiencyBonus(3)).willReturn(2);
        given(hitDieProvider.getHitDie("Barbarian")).willReturn("d12");
        given(modifierCalculator.calculateModifier(12)).willReturn(1);
        given(maxHpCalculator.calculateMaxHp("d12", 3, 1)).willReturn(25);
        given(spellcastingAbilityProvider.getSpellcastingAbility("Barbarian")).willReturn(null);
        given(repository.update(1L, input, compendiumData, 2, 25, null, null, null)).willReturn(expected);

        var result = sut.update(1L, input);

        assertThat(result).isEqualTo(expected);
        then(validationService).should().validate(input);
        then(repository).should().update(1L, input, compendiumData, 2, 25, null, null, null);
    }

    @Test
    void shouldUpdateSpellcasterWithComputedSpellStats(@Random ValidatedCompendiumData compendiumData,
                                                       @Random Character expected) {
        // Cleric with WIS=14 → modifier=2; profBonus=3; saveDc=8+3+2=13; attackBonus=3+2=5
        AbilityScores scores = AbilityScoresBuilder.builder()
                .withConstitution(10).withStrength(10).withDexterity(10)
                .withIntelligence(10).withWisdom(14).withCharisma(10).build();
        CharacterCreate input = CharacterCreateBuilder.builder()
                .withName("Aldric").withCharacterClass("Cleric").withLevel(5)
                .withAbilityScores(scores).build();

        given(validationService.validate(input)).willReturn(compendiumData);
        given(proficiencyBonusCalculator.calculateProficiencyBonus(5)).willReturn(3);
        given(hitDieProvider.getHitDie("Cleric")).willReturn("d8");
        given(modifierCalculator.calculateModifier(10)).willReturn(0);
        given(maxHpCalculator.calculateMaxHp("d8", 5, 0)).willReturn(22);
        given(spellcastingAbilityProvider.getSpellcastingAbility("Cleric")).willReturn("WIS");
        given(repository.update(2L, input, compendiumData, 3, 22, "WIS", 13, 5)).willReturn(expected);

        var result = sut.update(2L, input);

        assertThat(result).isEqualTo(expected);
        then(repository).should().update(2L, input, compendiumData, 3, 22, "WIS", 13, 5);
    }
}

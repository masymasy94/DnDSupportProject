package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.AbilityScores;
import com.dndplatform.character.domain.model.AbilityScoresBuilder;
import com.dndplatform.character.domain.model.Character;
import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.domain.model.CharacterCreateBuilder;
import com.dndplatform.character.domain.model.ValidatedCompendiumData;
import com.dndplatform.character.domain.repository.CharacterCreateRepository;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterCreateServiceImplTest {

    @Mock private CharacterValidationService validationService;
    @Mock private CharacterProficiencyBonusCalculator proficiencyBonusCalculator;
    @Mock private CharacterModifierCalculator modifierCalculator;
    @Mock private CharacterMaxHpCalculator maxHpCalculator;
    @Mock private CharacterHitDieProvider hitDieProvider;
    @Mock private CharacterSpellcastingAbilityProvider spellcastingAbilityProvider;
    @Mock private CharacterCreateRepository repository;

    private CharacterCreateServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterCreateServiceImpl(
                validationService, proficiencyBonusCalculator, modifierCalculator,
                maxHpCalculator, hitDieProvider, spellcastingAbilityProvider, repository);
    }

    @Test
    void shouldCreateNonSpellcasterCharacter(@Random ValidatedCompendiumData compendiumData,
                                             @Random Character expected) {
        AbilityScores scores = AbilityScoresBuilder.builder()
                .withConstitution(14).withStrength(16).withDexterity(12)
                .withIntelligence(10).withWisdom(10).withCharisma(10).build();
        CharacterCreate input = CharacterCreateBuilder.builder()
                .withName("Thorin").withCharacterClass("Fighter").withLevel(1)
                .withAbilityScores(scores).build();

        given(validationService.validate(input)).willReturn(compendiumData);
        given(proficiencyBonusCalculator.calculateProficiencyBonus(1)).willReturn(2);
        given(hitDieProvider.getHitDie("Fighter")).willReturn("d10");
        given(modifierCalculator.calculateModifier(14)).willReturn(2);
        given(maxHpCalculator.calculateMaxHp("d10", 1, 2)).willReturn(12);
        given(spellcastingAbilityProvider.getSpellcastingAbility("Fighter")).willReturn(null);
        given(repository.save(input, compendiumData, 2, 12, null, null, null)).willReturn(expected);

        var result = sut.create(input);

        assertThat(result).isEqualTo(expected);
        then(validationService).should().validate(input);
        then(repository).should().save(input, compendiumData, 2, 12, null, null, null);
    }

    @Test
    void shouldCreateSpellcasterWithComputedSpellStats(@Random ValidatedCompendiumData compendiumData,
                                                       @Random Character expected) {
        // Wizard with INT=16 → modifier=3; profBonus=2; saveDc=8+2+3=13; attackBonus=2+3=5
        AbilityScores scores = AbilityScoresBuilder.builder()
                .withConstitution(10).withStrength(8).withDexterity(14)
                .withIntelligence(16).withWisdom(12).withCharisma(10).build();
        CharacterCreate input = CharacterCreateBuilder.builder()
                .withName("Gandalf").withCharacterClass("Wizard").withLevel(5)
                .withAbilityScores(scores).build();

        given(validationService.validate(input)).willReturn(compendiumData);
        given(proficiencyBonusCalculator.calculateProficiencyBonus(5)).willReturn(3);
        given(hitDieProvider.getHitDie("Wizard")).willReturn("d6");
        given(modifierCalculator.calculateModifier(10)).willReturn(0);
        given(maxHpCalculator.calculateMaxHp("d6", 5, 0)).willReturn(20);
        given(spellcastingAbilityProvider.getSpellcastingAbility("Wizard")).willReturn("INT");
        given(repository.save(input, compendiumData, 3, 20, "INT", 14, 6)).willReturn(expected);

        var result = sut.create(input);

        assertThat(result).isEqualTo(expected);
        // spellSaveDc = 8 + 3 + 3 = 14, spellAttackBonus = 3 + 3 = 6
        then(repository).should().save(input, compendiumData, 3, 20, "INT", 14, 6);
    }
}

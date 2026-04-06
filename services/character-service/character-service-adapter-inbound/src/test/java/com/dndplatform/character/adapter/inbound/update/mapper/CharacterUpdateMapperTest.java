package com.dndplatform.character.adapter.inbound.update.mapper;

import com.dndplatform.character.domain.model.CharacterCreate;
import com.dndplatform.character.view.model.vm.*;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CharacterUpdateMapperTest {

    private CharacterUpdateMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterUpdateMapper();
    }

    @Test
    void shouldMapRequestToDomain(@Random UpdateCharacterRequest request) {
        CharacterCreate result = sut.apply(request);

        assertThat(result.name()).isEqualTo(request.name());
        assertThat(result.species()).isEqualTo(request.species());
        assertThat(result.subrace()).isEqualTo(request.subrace());
        assertThat(result.characterClass()).isEqualTo(request.characterClass());
        assertThat(result.subclass()).isEqualTo(request.subclass());
        assertThat(result.background()).isEqualTo(request.background());
        assertThat(result.alignment()).isEqualTo(request.alignment());
        assertThat(result.level()).isEqualTo(request.level());
        assertThat(result.skillProficiencies()).isEqualTo(request.skillProficiencies());
        assertThat(result.savingThrowProficiencies()).isEqualTo(request.savingThrowProficiencies());
        assertThat(result.languages()).isEqualTo(request.languages());
        assertThat(result.spells()).isEqualTo(request.spells());
        assertThat(result.personalityTraits()).isEqualTo(request.personalityTraits());
        assertThat(result.ideals()).isEqualTo(request.ideals());
        assertThat(result.bonds()).isEqualTo(request.bonds());
        assertThat(result.flaws()).isEqualTo(request.flaws());

        assertThat(result.abilityScores()).isNotNull();
        assertThat(result.abilityScores().strength()).isEqualTo(request.abilityScores().strength());
        assertThat(result.abilityScores().dexterity()).isEqualTo(request.abilityScores().dexterity());
        assertThat(result.abilityScores().constitution()).isEqualTo(request.abilityScores().constitution());
        assertThat(result.abilityScores().intelligence()).isEqualTo(request.abilityScores().intelligence());
        assertThat(result.abilityScores().wisdom()).isEqualTo(request.abilityScores().wisdom());
        assertThat(result.abilityScores().charisma()).isEqualTo(request.abilityScores().charisma());

        assertThat(result.physicalCharacteristics()).isNotNull();
        assertThat(result.physicalCharacteristics().age()).isEqualTo(request.physicalCharacteristics().age());
        assertThat(result.physicalCharacteristics().height()).isEqualTo(request.physicalCharacteristics().height());
        assertThat(result.physicalCharacteristics().weight()).isEqualTo(request.physicalCharacteristics().weight());
        assertThat(result.physicalCharacteristics().eyes()).isEqualTo(request.physicalCharacteristics().eyes());
        assertThat(result.physicalCharacteristics().skin()).isEqualTo(request.physicalCharacteristics().skin());
        assertThat(result.physicalCharacteristics().hair()).isEqualTo(request.physicalCharacteristics().hair());

        assertThat(result.proficiencies()).isNotNull();
        assertThat(result.proficiencies()).hasSameSizeAs(request.proficiencies());
        if (!result.proficiencies().isEmpty()) {
            assertThat(result.proficiencies().get(0).type()).isEqualTo(request.proficiencies().get(0).type());
            assertThat(result.proficiencies().get(0).name()).isEqualTo(request.proficiencies().get(0).name());
        }

        assertThat(result.equipment()).isNotNull();
        assertThat(result.equipment()).hasSameSizeAs(request.equipment());
        if (!result.equipment().isEmpty()) {
            assertThat(result.equipment().get(0).name()).isEqualTo(request.equipment().get(0).name());
            assertThat(result.equipment().get(0).quantity()).isEqualTo(request.equipment().get(0).quantity());
            assertThat(result.equipment().get(0).equipped()).isEqualTo(request.equipment().get(0).equipped());
        }
    }

    @Test
    void shouldHandleNullSubObjects() {
        var abilityScores = AbilityScoresRequestBuilder.builder()
                .withStrength(10)
                .withDexterity(10)
                .withConstitution(10)
                .withIntelligence(10)
                .withWisdom(10)
                .withCharisma(10)
                .build();
        var request = UpdateCharacterRequestBuilder.builder()
                .withUserId(1L)
                .withName("TestChar")
                .withSpecies("Human")
                .withCharacterClass("Fighter")
                .withLevel(1)
                .withAbilityScores(abilityScores)
                .withSubrace(null)
                .withSubclass(null)
                .withBackground(null)
                .withAlignment(null)
                .withSkillProficiencies(null)
                .withSavingThrowProficiencies(null)
                .withLanguages(null)
                .withProficiencies(null)
                .withEquipment(null)
                .withSpells(null)
                .withPhysicalCharacteristics(null)
                .withPersonalityTraits(null)
                .withIdeals(null)
                .withBonds(null)
                .withFlaws(null)
                .build();

        CharacterCreate result = sut.apply(request);

        assertThat(result.abilityScores()).isNotNull();
        assertThat(result.physicalCharacteristics()).isNull();
        assertThat(result.proficiencies()).isEqualTo(List.of());
        assertThat(result.equipment()).isEqualTo(List.of());
    }
}

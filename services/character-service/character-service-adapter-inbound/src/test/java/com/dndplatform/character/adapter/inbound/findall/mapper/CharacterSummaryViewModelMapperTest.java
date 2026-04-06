package com.dndplatform.character.adapter.inbound.findall.mapper;

import com.dndplatform.character.domain.model.CharacterSummary;
import com.dndplatform.character.view.model.vm.CharacterSummaryViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CharacterSummaryViewModelMapperTest {

    private CharacterSummaryViewModelMapper sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterSummaryViewModelMapper();
    }

    @Test
    void shouldMapSummaryToViewModel(@Random CharacterSummary summary) {
        CharacterSummaryViewModel result = sut.apply(summary);

        assertThat(result.id()).isEqualTo(summary.id());
        assertThat(result.name()).isEqualTo(summary.name());
        assertThat(result.species()).isEqualTo(summary.species());
        assertThat(result.characterClass()).isEqualTo(summary.characterClass());
        assertThat(result.level()).isEqualTo(summary.level());
        assertThat(result.hitPointsCurrent()).isEqualTo(summary.hitPointsCurrent());
        assertThat(result.hitPointsMax()).isEqualTo(summary.hitPointsMax());
        assertThat(result.armorClass()).isEqualTo(summary.armorClass());
    }
}

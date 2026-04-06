package com.dndplatform.chat.domain.impl;

import com.dndplatform.chat.domain.DiceRollService;
import com.dndplatform.chat.domain.model.DiceGroup;
import com.dndplatform.chat.domain.model.DiceRollResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DiceRollServiceImplTest {

    private DiceRollService sut;

    @BeforeEach
    void setUp() {
        sut = new DiceRollServiceImpl();
    }

    @Test
    void shouldRollSingleDie() {
        DiceRollResult result = sut.roll("d20");

        assertThat(result).isNotNull();
        assertThat(result.formula()).isEqualTo("d20");
        assertThat(result.groups()).hasSize(1);
        assertThat(result.groups().getFirst().sides()).isEqualTo(20);
        assertThat(result.groups().getFirst().count()).isEqualTo(1);
        assertThat(result.groups().getFirst().rolls()).hasSize(1);
        assertThat(result.groups().getFirst().rolls().getFirst()).isBetween(1, 20);
    }

    @Test
    void shouldRollMultipleDice() {
        DiceRollResult result = sut.roll("2d6");

        assertThat(result).isNotNull();
        assertThat(result.formula()).isEqualTo("2d6");
        assertThat(result.groups()).hasSize(1);
        assertThat(result.groups().getFirst().count()).isEqualTo(2);
        assertThat(result.groups().getFirst().sides()).isEqualTo(6);
        assertThat(result.groups().getFirst().rolls()).hasSize(2);
        result.groups().getFirst().rolls().forEach(r -> assertThat(r).isBetween(1, 6));
    }

    @Test
    void shouldRollWithModifier() {
        DiceRollResult result = sut.roll("2d6+5");

        assertThat(result).isNotNull();
        assertThat(result.formula()).isEqualTo("2d6+5");
        assertThat(result.modifier()).isEqualTo(5);
        assertThat(result.total()).isEqualTo(result.groups().stream().mapToInt(DiceGroup::subtotal).sum() + 5);
    }

    @Test
    void shouldRollWithNegativeModifier() {
        DiceRollResult result = sut.roll("1d8-2");

        assertThat(result).isNotNull();
        assertThat(result.modifier()).isEqualTo(-2);
    }

    @Test
    void shouldHandleMultipleDiceGroups() {
        DiceRollResult result = sut.roll("2d6+1d8+3");

        assertThat(result).isNotNull();
        assertThat(result.groups()).hasSize(2);
    }

    @Test
    void shouldThrowExceptionWhenFormulaIsNull() {
        assertThatThrownBy(() -> sut.roll(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dice formula cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenFormulaIsBlank() {
        assertThatThrownBy(() -> sut.roll("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Dice formula cannot be empty");
    }

    @Test
    void shouldThrowExceptionWhenNoDiceInFormula() {
        assertThatThrownBy(() -> sut.roll("5+3"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no dice groups found");
    }

    @Test
    void shouldThrowExceptionWhenInvalidDiceGroup() {
        assertThatThrownBy(() -> sut.roll("0d6"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid dice group");
    }

    @Test
    void shouldThrowExceptionWhenInvalidFormula() {
        assertThatThrownBy(() -> sut.roll("abc"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid dice formula");
    }
}

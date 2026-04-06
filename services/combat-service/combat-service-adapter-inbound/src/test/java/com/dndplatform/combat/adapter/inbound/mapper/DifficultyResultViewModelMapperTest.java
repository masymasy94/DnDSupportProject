package com.dndplatform.combat.adapter.inbound.mapper;

import com.dndplatform.combat.domain.model.DifficultyRating;
import com.dndplatform.combat.domain.model.DifficultyResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class DifficultyResultViewModelMapperTest {

    private DifficultyResultViewModelMapper sut;

    @BeforeEach
    void setUp() {
        sut = new DifficultyResultViewModelMapper();
    }

    @Test
    void shouldMapAllFields() {
        var thresholds = new LinkedHashMap<DifficultyRating, Integer>();
        thresholds.put(DifficultyRating.EASY, 300);
        thresholds.put(DifficultyRating.MEDIUM, 600);
        thresholds.put(DifficultyRating.HARD, 900);
        thresholds.put(DifficultyRating.DEADLY, 1400);

        var domain = new DifficultyResult(DifficultyRating.MEDIUM, 450, 900, thresholds, 3, 4);

        var result = sut.apply(domain);

        assertThat(result.rating()).isEqualTo(DifficultyRating.MEDIUM.name());
        assertThat(result.totalMonsterXp()).isEqualTo(450);
        assertThat(result.adjustedXp()).isEqualTo(900);
        assertThat(result.partyLevel()).isEqualTo(3);
        assertThat(result.partySize()).isEqualTo(4);
        assertThat(result.partyXpThresholds()).containsEntry("EASY", 300);
        assertThat(result.partyXpThresholds()).containsEntry("MEDIUM", 600);
        assertThat(result.partyXpThresholds()).containsEntry("HARD", 900);
        assertThat(result.partyXpThresholds()).containsEntry("DEADLY", 1400);
    }

    @Test
    void shouldMapNullRatingToNull() {
        var domain = new DifficultyResult(null, 0, 0, null, 1, 4);

        var result = sut.apply(domain);

        assertThat(result.rating()).isNull();
        assertThat(result.partyXpThresholds()).isEmpty();
    }
}

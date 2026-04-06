package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.DifficultyCalculateService;
import com.dndplatform.combat.domain.DifficultyCalculateServiceImpl;
import com.dndplatform.combat.domain.model.DifficultyRating;
import com.dndplatform.combat.domain.model.DifficultyResult;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantType;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DifficultyCalculateServiceImplTest {

    @Mock
    private EncounterFindByIdRepository findRepository;

    private DifficultyCalculateService sut;

    @BeforeEach
    void setUp() {
        sut = new DifficultyCalculateServiceImpl(findRepository);
    }

    @Test
    void shouldCalculateMediumDifficulty(@Random Long encounterId) {
        List<EncounterParticipant> participants = List.of(
                new EncounterParticipant(1L, encounterId, "Goblin", ParticipantType.MONSTER, 0, 0, 7, 15, List.of(), false, 0, 1L, "{\"xp\": \"100\"}")
        );
        Encounter encounter = new Encounter(encounterId, 1L, 2L, "Dragon", "desc", null, 5, 4, null, participants, null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(encounter));

        DifficultyResult result = sut.calculate(encounterId);

        assertThat(result.rating()).isNotNull();
        assertThat(result.partyLevel()).isEqualTo(5);
        assertThat(result.partySize()).isEqualTo(4);

        var inOrder = inOrder(findRepository);
        then(findRepository).should(inOrder).findById(encounterId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotFound() {
        Long encounterId = 999L;

        given(findRepository.findById(encounterId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.calculate(encounterId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Encounter not found with ID: 999");
    }

    @Test
    void shouldReturnTrivialWithNoMonsters(@Random Long encounterId) {
        List<EncounterParticipant> participants = List.of(
                new EncounterParticipant(1L, encounterId, "Player", ParticipantType.PC, 0, 0, 18, 10, List.of(), false, 0, null, null)
        );
        Encounter encounter = new Encounter(encounterId, 1L, 2L, "Empty", "desc", null, 5, 4, null, participants, null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(encounter));

        DifficultyResult result = sut.calculate(encounterId);

        assertThat(result.rating()).isEqualTo(DifficultyRating.TRIVIAL);
    }
}

package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.TurnAdvanceService;
import com.dndplatform.combat.domain.TurnAdvanceServiceImpl;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.model.ParticipantType;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.ParticipantAdvanceTurnRepository;
import com.dndplatform.combat.domain.repository.ParticipantFindByEncounterRepository;
import com.dndplatform.common.exception.ForbiddenException;
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
import static org.mockito.BDDMockito.doNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class TurnAdvanceServiceImplTest {

    @Mock
    private EncounterFindByIdRepository findRepository;
    @Mock
    private ParticipantAdvanceTurnRepository advanceTurnRepository;
    @Mock
    private ParticipantFindByEncounterRepository participantFindRepository;

    private TurnAdvanceService sut;

    @BeforeEach
    void setUp() {
        sut = new TurnAdvanceServiceImpl(findRepository, advanceTurnRepository, participantFindRepository);
    }

    @Test
    void shouldAdvanceTurn(@Random Long encounterId, @Random Long userId) {
        List<EncounterParticipant> participants = List.of(
                new EncounterParticipant(1L, encounterId, "Goblin", ParticipantType.MONSTER, 0, 0, 7, 15, List.of(), false, 0, 1L, null),
                new EncounterParticipant(2L, encounterId, "Player", ParticipantType.PC, 0, 0, 18, 10, List.of(), false, 0, null, null)
        );
        Encounter existing = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.ACTIVE, 5, 4, null, participants, null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));
        doNothing().when(advanceTurnRepository).advanceTurn(encounterId);
        given(participantFindRepository.findByEncounterId(encounterId)).willReturn(participants);

        List<EncounterParticipant> result = sut.advance(encounterId, userId);

        assertThat(result).isEqualTo(participants);

        var inOrder = inOrder(findRepository, advanceTurnRepository, participantFindRepository);
        then(findRepository).should(inOrder).findById(encounterId);
        then(advanceTurnRepository).should(inOrder).advanceTurn(encounterId);
        then(participantFindRepository).should(inOrder).findByEncounterId(encounterId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotFound() {
        Long encounterId = 999L;
        Long userId = 2L;

        given(findRepository.findById(encounterId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.advance(encounterId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Encounter not found with ID: 999");
    }

    @Test
    void shouldThrowWhenUserNotAuthorized(@Random Long encounterId) {
        // userId (3L) differs from dungeon master (2L) to exercise authorization check
        Long userId = 3L;
        List<EncounterParticipant> participants = List.of(
                new EncounterParticipant(1L, encounterId, "Goblin", ParticipantType.MONSTER, 0, 0, 7, 15, List.of(), false, 0, 1L, null)
        );
        Encounter existing = new Encounter(encounterId, 1L, 2L, "Dragon", "desc", EncounterStatus.ACTIVE, 5, 4, null, participants, null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.advance(encounterId, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(advanceTurnRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotActive(@Random Long encounterId, @Random Long userId) {
        List<EncounterParticipant> participants = List.of(
                new EncounterParticipant(1L, encounterId, "Goblin", ParticipantType.MONSTER, 0, 0, 7, 15, List.of(), false, 0, 1L, null)
        );
        Encounter existing = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.DRAFT, 5, 4, null, participants, null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.advance(encounterId, userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ACTIVE status");

        then(advanceTurnRepository).shouldHaveNoInteractions();
    }
}

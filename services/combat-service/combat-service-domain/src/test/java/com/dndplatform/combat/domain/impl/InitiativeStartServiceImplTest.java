package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.InitiativeStartService;
import com.dndplatform.combat.domain.InitiativeStartServiceImpl;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.model.ParticipantType;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.EncounterUpdateStatusRepository;
import com.dndplatform.combat.domain.repository.ParticipantSetInitiativeRepository;
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
import static org.mockito.Mockito.any;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class InitiativeStartServiceImplTest {

    @Mock
    private EncounterFindByIdRepository findRepository;
    @Mock
    private ParticipantSetInitiativeRepository setInitiativeRepository;
    @Mock
    private EncounterUpdateStatusRepository updateStatusRepository;

    private InitiativeStartService sut;

    @BeforeEach
    void setUp() {
        sut = new InitiativeStartServiceImpl(findRepository, setInitiativeRepository, updateStatusRepository);
    }

    @Test
    void shouldStartInitiative(@Random Long encounterId, @Random Long userId) {
        List<EncounterParticipant> participants = List.of(
                new EncounterParticipant(1L, encounterId, "Goblin", ParticipantType.MONSTER, 0, 0, 7, 15, List.of(), false, 0, 1L, null),
                new EncounterParticipant(2L, encounterId, "Player", ParticipantType.PC, 0, 0, 18, 10, List.of(), false, 0, null, null)
        );
        Encounter existing = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.DRAFT, 5, 4, null, participants, null, null);
        Encounter active = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.ACTIVE, 5, 4, null, participants, null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing), Optional.of(active));
        doNothing().when(setInitiativeRepository).setInitiatives(any(), any());
        doNothing().when(updateStatusRepository).updateStatus(encounterId, EncounterStatus.ACTIVE);

        Encounter result = sut.start(encounterId, userId);

        assertThat(result.status()).isEqualTo(EncounterStatus.ACTIVE);

        var inOrder = inOrder(findRepository, setInitiativeRepository, updateStatusRepository);
        then(findRepository).should(inOrder).findById(encounterId);
        then(setInitiativeRepository).should(inOrder).setInitiatives(any(), any());
        then(updateStatusRepository).should(inOrder).updateStatus(encounterId, EncounterStatus.ACTIVE);
        then(findRepository).should(inOrder).findById(encounterId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotFound() {
        Long encounterId = 999L;
        Long userId = 2L;

        given(findRepository.findById(encounterId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.start(encounterId, userId))
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
        Encounter existing = new Encounter(encounterId, 1L, 2L, "Dragon", "desc", EncounterStatus.DRAFT, 5, 4, null, participants, null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.start(encounterId, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(setInitiativeRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotDraft(@Random Long encounterId, @Random Long userId) {
        List<EncounterParticipant> participants = List.of(
                new EncounterParticipant(1L, encounterId, "Goblin", ParticipantType.MONSTER, 0, 0, 7, 15, List.of(), false, 0, 1L, null)
        );
        Encounter existing = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.ACTIVE, 5, 4, null, participants, null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.start(encounterId, userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("DRAFT status");

        then(setInitiativeRepository).shouldHaveNoInteractions();
    }
}

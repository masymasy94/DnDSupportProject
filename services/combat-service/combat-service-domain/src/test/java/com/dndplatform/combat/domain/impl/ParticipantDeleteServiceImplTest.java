package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.ParticipantDeleteService;
import com.dndplatform.combat.domain.ParticipantDeleteServiceImpl;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.ParticipantDeleteRepository;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.doNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantDeleteServiceImplTest {

    @Mock
    private EncounterFindByIdRepository findRepository;
    @Mock
    private ParticipantDeleteRepository deleteRepository;

    private ParticipantDeleteService sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantDeleteServiceImpl(findRepository, deleteRepository);
    }

    @Test
    void shouldDeleteParticipant(@Random Long encounterId, @Random Long participantId, @Random Long userId) {
        Encounter existing = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.DRAFT, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));
        doNothing().when(deleteRepository).deleteById(participantId);

        sut.delete(encounterId, participantId, userId);

        var inOrder = inOrder(findRepository, deleteRepository);
        then(findRepository).should(inOrder).findById(encounterId);
        then(deleteRepository).should(inOrder).deleteById(participantId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotFound() {
        Long encounterId = 999L;
        Long participantId = 2L;
        Long userId = 2L;

        given(findRepository.findById(encounterId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.delete(encounterId, participantId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Encounter not found with ID: 999");

        then(deleteRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowWhenUserNotAuthorized(@Random Long encounterId, @Random Long participantId) {
        // userId (3L) differs from dungeon master (2L) to exercise authorization check
        Long userId = 3L;
        Encounter existing = new Encounter(encounterId, 1L, 2L, "Dragon", "desc", EncounterStatus.DRAFT, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.delete(encounterId, participantId, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(deleteRepository).shouldHaveNoInteractions();
    }
}

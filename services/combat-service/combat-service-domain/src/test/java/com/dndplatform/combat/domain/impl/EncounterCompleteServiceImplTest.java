package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.EncounterCompleteService;
import com.dndplatform.combat.domain.EncounterCompleteServiceImpl;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.EncounterUpdateStatusRepository;
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
class EncounterCompleteServiceImplTest {

    @Mock
    private EncounterFindByIdRepository findRepository;
    @Mock
    private EncounterUpdateStatusRepository updateStatusRepository;

    private EncounterCompleteService sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterCompleteServiceImpl(findRepository, updateStatusRepository);
    }

    @Test
    void shouldCompleteEncounter(@Random Long encounterId, @Random Long userId) {
        Encounter existing = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.ACTIVE, 5, 4, null, List.of(), null, null);
        Encounter completed = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.COMPLETED, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing), Optional.of(completed));
        doNothing().when(updateStatusRepository).updateStatus(encounterId, EncounterStatus.COMPLETED);

        Encounter result = sut.complete(encounterId, userId);

        assertThat(result.status()).isEqualTo(EncounterStatus.COMPLETED);

        var inOrder = inOrder(findRepository, updateStatusRepository);
        then(findRepository).should(inOrder).findById(encounterId);
        then(updateStatusRepository).should(inOrder).updateStatus(encounterId, EncounterStatus.COMPLETED);
        then(findRepository).should(inOrder).findById(encounterId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotFound() {
        Long encounterId = 999L;
        Long userId = 2L;

        given(findRepository.findById(encounterId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.complete(encounterId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Encounter not found with ID: 999");
    }

    @Test
    void shouldThrowWhenUserNotAuthorized(@Random Long encounterId) {
        // userId (3L) differs from dungeon master (2L) to exercise authorization check
        Long userId = 3L;
        Encounter existing = new Encounter(encounterId, 1L, 2L, "Dragon", "desc", EncounterStatus.ACTIVE, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.complete(encounterId, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(updateStatusRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotActive(@Random Long encounterId, @Random Long userId) {
        Encounter existing = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.DRAFT, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.complete(encounterId, userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ACTIVE status");

        then(updateStatusRepository).shouldHaveNoInteractions();
    }
}

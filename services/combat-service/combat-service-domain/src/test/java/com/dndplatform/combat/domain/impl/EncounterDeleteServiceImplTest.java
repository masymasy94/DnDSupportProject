package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.EncounterDeleteService;
import com.dndplatform.combat.domain.EncounterDeleteServiceImpl;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterDeleteRepository;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
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
class EncounterDeleteServiceImplTest {

    @Mock
    private EncounterFindByIdRepository findRepository;
    @Mock
    private EncounterDeleteRepository deleteRepository;

    private EncounterDeleteService sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterDeleteServiceImpl(findRepository, deleteRepository);
    }

    @Test
    void shouldDeleteEncounter(@Random Long encounterId, @Random Long userId) {
        Encounter existing = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.DRAFT, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));
        doNothing().when(deleteRepository).deleteById(encounterId);

        sut.delete(encounterId, userId);

        var inOrder = inOrder(findRepository, deleteRepository);
        then(findRepository).should(inOrder).findById(encounterId);
        then(deleteRepository).should(inOrder).deleteById(encounterId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotFound() {
        Long encounterId = 999L;
        Long userId = 2L;

        given(findRepository.findById(encounterId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.delete(encounterId, userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Encounter not found with ID: 999");

        then(deleteRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowWhenUserNotAuthorized(@Random Long encounterId) {
        // userId (3L) differs from dungeon master (2L) to exercise authorization check
        Long userId = 3L;
        Encounter existing = new Encounter(encounterId, 1L, 2L, "Dragon", "desc", EncounterStatus.DRAFT, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.delete(encounterId, userId))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(deleteRepository).shouldHaveNoInteractions();
    }
}

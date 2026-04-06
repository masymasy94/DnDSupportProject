package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.EncounterUpdateService;
import com.dndplatform.combat.domain.EncounterUpdateServiceImpl;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.model.EncounterUpdate;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.EncounterUpdateRepository;
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

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EncounterUpdateServiceImplTest {

    @Mock
    private EncounterFindByIdRepository findRepository;
    @Mock
    private EncounterUpdateRepository updateRepository;

    private EncounterUpdateService sut;

    @BeforeEach
    void setUp() {
        sut = new EncounterUpdateServiceImpl(findRepository, updateRepository);
    }

    @Test
    void shouldUpdateEncounter(@Random Long userId, @Random Encounter expected) {
        EncounterUpdate input = new EncounterUpdate(1L, "New Name", "desc", 5, 4, List.of());
        Encounter existing = new Encounter(1L, 1L, userId, "Old Name", "old", EncounterStatus.DRAFT, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(1L)).willReturn(Optional.of(existing));
        given(updateRepository.update(input)).willReturn(expected);

        Encounter result = sut.update(userId, input);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(findRepository, updateRepository);
        then(findRepository).should(inOrder).findById(1L);
        then(updateRepository).should(inOrder).update(input);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotFound() {
        Long userId = 2L;
        EncounterUpdate input = new EncounterUpdate(999L, "Name", "desc", 5, 4, List.of());

        given(findRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.update(userId, input))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Encounter not found with ID: 999");
    }

    @Test
    void shouldThrowWhenUserNotAuthorized() {
        // userId (3L) differs from dungeon master (2L) to exercise authorization check
        Long userId = 3L;
        EncounterUpdate input = new EncounterUpdate(1L, "Name", "desc", 5, 4, List.of());
        Encounter existing = new Encounter(1L, 1L, 2L, "Old Name", "old", EncounterStatus.DRAFT, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(1L)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.update(userId, input))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(updateRepository).shouldHaveNoInteractions();
    }
}

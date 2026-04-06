package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.ParticipantUpdateService;
import com.dndplatform.combat.domain.ParticipantUpdateServiceImpl;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.model.ParticipantUpdate;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.ParticipantUpdateRepository;
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
class ParticipantUpdateServiceImplTest {

    @Mock
    private EncounterFindByIdRepository findRepository;
    @Mock
    private ParticipantUpdateRepository updateRepository;

    private ParticipantUpdateService sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantUpdateServiceImpl(findRepository, updateRepository);
    }

    @Test
    void shouldUpdateParticipant(@Random Long encounterId, @Random Long userId, @Random ParticipantUpdate input, @Random EncounterParticipant updated) {
        Encounter existing = new Encounter(encounterId, 1L, userId, "Dragon", "desc", EncounterStatus.DRAFT, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));
        given(updateRepository.update(input)).willReturn(updated);

        EncounterParticipant result = sut.update(encounterId, userId, input);

        assertThat(result).isEqualTo(updated);

        var inOrder = inOrder(findRepository, updateRepository);
        then(findRepository).should(inOrder).findById(encounterId);
        then(updateRepository).should(inOrder).update(input);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotFound(@Random ParticipantUpdate input) {
        Long encounterId = 999L;
        Long userId = 2L;

        given(findRepository.findById(encounterId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.update(encounterId, userId, input))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Encounter not found with ID: 999");
    }

    @Test
    void shouldThrowWhenUserNotAuthorized(@Random Long encounterId, @Random ParticipantUpdate input) {
        // userId (3L) differs from dungeon master (2L) to exercise authorization check
        Long userId = 3L;
        Encounter existing = new Encounter(encounterId, 1L, 2L, "Dragon", "desc", EncounterStatus.DRAFT, 5, 4, null, List.of(), null, null);

        given(findRepository.findById(encounterId)).willReturn(Optional.of(existing));

        assertThatThrownBy(() -> sut.update(encounterId, userId, input))
                .isInstanceOf(ForbiddenException.class)
                .hasMessageContaining("Dungeon Master");

        then(updateRepository).shouldHaveNoInteractions();
    }
}

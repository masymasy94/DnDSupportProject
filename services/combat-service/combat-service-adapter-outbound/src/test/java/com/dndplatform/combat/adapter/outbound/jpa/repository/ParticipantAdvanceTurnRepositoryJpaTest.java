package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantAdvanceTurnRepositoryJpaTest {

    @Mock
    private ParticipantPanacheRepository participantRepository;

    private ParticipantAdvanceTurnRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantAdvanceTurnRepositoryJpa(participantRepository);
    }

    @Test
    void shouldDoNothingWhenNoParticipantsExist(@Random Long encounterId) {
        given(participantRepository.findByEncounterIdOrderBySortOrder(encounterId)).willReturn(List.of());

        sut.advanceTurn(encounterId);

        then(participantRepository).should().findByEncounterIdOrderBySortOrder(encounterId);
        then(participantRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldActivateFirstParticipantWhenNoneIsActive(@Random Long encounterId) {
        var first = new EncounterParticipantEntity();
        first.isActive = false;
        var second = new EncounterParticipantEntity();
        second.isActive = false;

        given(participantRepository.findByEncounterIdOrderBySortOrder(encounterId))
                .willReturn(List.of(first, second));
        willDoNothing().given(participantRepository).persist(any(EncounterParticipantEntity.class));

        sut.advanceTurn(encounterId);

        // When activeIndex is -1, nextIndex = (-1+1)%2 = 0, so first becomes active
        assertThat(first.isActive).isTrue();
        then(participantRepository).should().persist(first);
    }

    @Test
    void shouldDeactivateCurrentAndActivateNextParticipant(@Random Long encounterId) {
        var current = new EncounterParticipantEntity();
        current.isActive = true;
        var next = new EncounterParticipantEntity();
        next.isActive = false;

        given(participantRepository.findByEncounterIdOrderBySortOrder(encounterId))
                .willReturn(List.of(current, next));
        willDoNothing().given(participantRepository).persist(any(EncounterParticipantEntity.class));

        sut.advanceTurn(encounterId);

        assertThat(current.isActive).isFalse();
        assertThat(next.isActive).isTrue();
        then(participantRepository).should().persist(current);
        then(participantRepository).should().persist(next);
    }
}

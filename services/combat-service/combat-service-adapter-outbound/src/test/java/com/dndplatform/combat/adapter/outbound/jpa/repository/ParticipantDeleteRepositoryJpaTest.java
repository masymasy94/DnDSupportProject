package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantDeleteRepositoryJpaTest {

    @Mock
    private ParticipantPanacheRepository participantRepository;

    private ParticipantDeleteRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantDeleteRepositoryJpa(participantRepository);
    }

    @Test
    void shouldDeleteParticipantWhenFound(@Random Long participantId) {
        var encounter = new EncounterEntity();
        var entity = new EncounterParticipantEntity();
        entity.encounter = encounter;
        encounter.participants = new ArrayList<>();
        encounter.participants.add(entity);

        given(participantRepository.findByIdOptional(participantId)).willReturn(Optional.of(entity));
        willDoNothing().given(participantRepository).delete(entity);

        sut.deleteById(participantId);

        then(participantRepository).should().findByIdOptional(participantId);
        then(participantRepository).should().delete(entity);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenParticipantMissing(@Random Long participantId) {
        given(participantRepository.findByIdOptional(participantId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.deleteById(participantId))
                .isInstanceOf(NotFoundException.class);

        then(participantRepository).should().findByIdOptional(participantId);
        then(participantRepository).shouldHaveNoMoreInteractions();
    }
}

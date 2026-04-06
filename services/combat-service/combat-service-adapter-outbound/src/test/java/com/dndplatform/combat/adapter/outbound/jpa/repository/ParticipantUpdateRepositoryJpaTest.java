package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantUpdate;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantUpdateRepositoryJpaTest {

    @Mock
    private EncounterEntityMapper mapper;

    @Mock
    private ParticipantPanacheRepository participantRepository;

    private ParticipantUpdateRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantUpdateRepositoryJpa(mapper, participantRepository);
    }

    @Test
    void shouldUpdateParticipantAndReturnMapped(@Random Long participantId,
                                                 @Random EncounterParticipant expected) {
        var entity = new EncounterParticipantEntity();
        var input = new ParticipantUpdate(participantId, 50, null, true);

        given(participantRepository.findByIdOptional(participantId)).willReturn(Optional.of(entity));
        willDoNothing().given(participantRepository).persist(any(EncounterParticipantEntity.class));
        given(mapper.toParticipant(entity)).willReturn(expected);

        var result = sut.update(input);

        assertThat(result).isEqualTo(expected);
        assertThat(entity.currentHp).isEqualTo(50);
        assertThat(entity.isActive).isTrue();
        then(participantRepository).should().findByIdOptional(participantId);
        then(participantRepository).should().persist(entity);
        then(mapper).should().toParticipant(entity);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenParticipantMissing(@Random Long participantId) {
        given(participantRepository.findByIdOptional(participantId)).willReturn(Optional.empty());
        var input = new ParticipantUpdate(participantId, 50, null, true);

        assertThatThrownBy(() -> sut.update(input))
                .isInstanceOf(NotFoundException.class);

        then(participantRepository).should().findByIdOptional(participantId);
        then(participantRepository).shouldHaveNoMoreInteractions();
    }
}

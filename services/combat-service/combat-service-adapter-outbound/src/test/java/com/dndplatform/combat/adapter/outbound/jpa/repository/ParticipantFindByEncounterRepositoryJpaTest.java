package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantFindByEncounterRepositoryJpaTest {

    @Mock
    private EncounterEntityMapper mapper;

    @Mock
    private ParticipantPanacheRepository participantRepository;

    private ParticipantFindByEncounterRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantFindByEncounterRepositoryJpa(mapper, participantRepository);
    }

    @Test
    void shouldReturnMappedParticipantsOrderedBySortOrder(@Random Long encounterId,
                                                            @Random EncounterParticipantEntity entity,
                                                            @Random EncounterParticipant expected) {
        given(participantRepository.findByEncounterIdOrderBySortOrder(encounterId)).willReturn(List.of(entity));
        given(mapper.toParticipant(entity)).willReturn(expected);

        var result = sut.findByEncounterId(encounterId);

        assertThat(result).containsExactly(expected);
        then(participantRepository).should().findByEncounterIdOrderBySortOrder(encounterId);
        then(mapper).should().toParticipant(entity);
    }

    @Test
    void shouldReturnEmptyListWhenNoParticipantsFound(@Random Long encounterId) {
        given(participantRepository.findByEncounterIdOrderBySortOrder(encounterId)).willReturn(List.of());

        var result = sut.findByEncounterId(encounterId);

        assertThat(result).isEmpty();
        then(mapper).shouldHaveNoInteractions();
    }
}

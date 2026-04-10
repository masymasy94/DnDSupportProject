package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.TurnOrderFindService;
import com.dndplatform.combat.domain.TurnOrderFindServiceImpl;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.ParticipantFindByEncounterRepository;
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
class TurnOrderFindServiceImplTest {

    @Mock
    private ParticipantFindByEncounterRepository repository;
    @Mock
    private EncounterFindByIdRepository encounterFindByIdRepository;

    private TurnOrderFindService sut;

    @BeforeEach
    void setUp() {
        sut = new TurnOrderFindServiceImpl(repository, encounterFindByIdRepository);
    }

    @Test
    void shouldGetTurnOrder(@Random Long encounterId, @Random Encounter encounter,
                            @Random EncounterParticipant participant1, @Random EncounterParticipant participant2) {
        List<EncounterParticipant> expected = List.of(participant1, participant2);

        given(encounterFindByIdRepository.findById(encounterId)).willReturn(Optional.of(encounter));
        given(repository.findByEncounterId(encounterId)).willReturn(expected);

        List<EncounterParticipant> result = sut.getTurnOrder(encounterId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(encounterFindByIdRepository, repository);
        then(encounterFindByIdRepository).should(inOrder).findById(encounterId);
        then(repository).should(inOrder).findByEncounterId(encounterId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenEncounterNotFound(@Random Long encounterId) {
        given(encounterFindByIdRepository.findById(encounterId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> sut.getTurnOrder(encounterId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Encounter not found");
    }
}

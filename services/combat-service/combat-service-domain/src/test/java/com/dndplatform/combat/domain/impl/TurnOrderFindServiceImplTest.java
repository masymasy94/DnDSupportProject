package com.dndplatform.combat.domain.impl;

import com.dndplatform.combat.domain.TurnOrderFindService;
import com.dndplatform.combat.domain.TurnOrderFindServiceImpl;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.repository.ParticipantFindByEncounterRepository;
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
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class TurnOrderFindServiceImplTest {

    @Mock
    private ParticipantFindByEncounterRepository repository;

    private TurnOrderFindService sut;

    @BeforeEach
    void setUp() {
        sut = new TurnOrderFindServiceImpl(repository);
    }

    @Test
    void shouldGetTurnOrder(@Random Long encounterId, @Random EncounterParticipant participant1, @Random EncounterParticipant participant2) {
        List<EncounterParticipant> expected = List.of(participant1, participant2);

        given(repository.findByEncounterId(encounterId)).willReturn(expected);

        List<EncounterParticipant> result = sut.getTurnOrder(encounterId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findByEncounterId(encounterId);
        inOrder.verifyNoMoreInteractions();
    }
}

package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.domain.ParticipantDeleteService;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantDeleteDelegateTest {

    @Mock
    private ParticipantDeleteService service;

    private ParticipantDeleteDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantDeleteDelegate(service);
    }

    @Test
    void shouldDelegateToService(@Random Long encounterId,
                                 @Random Long participantId,
                                 @Random Long userId) {
        willDoNothing().given(service).delete(encounterId, participantId, userId);

        sut.delete(encounterId, participantId, userId);

        var inOrder = inOrder(service);
        then(service).should(inOrder).delete(encounterId, participantId, userId);
    }

    @Test
    void shouldThrowWhenParticipantNotFound(@Random Long encounterId,
                                            @Random Long participantId,
                                            @Random Long userId) {
        willThrow(new NotFoundException("Participant not found"))
                .given(service).delete(encounterId, participantId, userId);

        assertThatThrownBy(() -> sut.delete(encounterId, participantId, userId))
                .isInstanceOf(NotFoundException.class);
    }
}

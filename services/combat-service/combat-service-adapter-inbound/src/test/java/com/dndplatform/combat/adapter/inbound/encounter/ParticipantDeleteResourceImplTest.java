package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantDeleteResourceImplTest {

    @Mock
    private ParticipantDeleteDelegate delegate;

    private ParticipantDeleteResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantDeleteResourceImpl(delegate);
    }

    @Test
    void shouldDelegateDelete(@Random Long encounterId, @Random Long participantId, @Random Long userId) {
        willDoNothing().given(delegate).delete(encounterId, participantId, userId);

        sut.delete(encounterId, participantId, userId);

        then(delegate).should().delete(encounterId, participantId, userId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}

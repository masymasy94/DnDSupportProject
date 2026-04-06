package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
import com.dndplatform.combat.view.model.vm.UpdateParticipantRequest;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantUpdateResourceImplTest {

    @Mock
    private ParticipantUpdateDelegate delegate;

    private ParticipantUpdateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantUpdateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateUpdate(@Random Long encounterId, @Random Long participantId, @Random UpdateParticipantRequest request, @Random ParticipantViewModel expected) {
        given(delegate.update(encounterId, participantId, request)).willReturn(expected);

        var result = sut.update(encounterId, participantId, request);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().update(encounterId, participantId, request);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}

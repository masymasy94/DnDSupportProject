package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.vm.AddParticipantRequest;
import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
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
class ParticipantAddResourceImplTest {

    @Mock
    private ParticipantAddDelegate delegate;

    private ParticipantAddResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantAddResourceImpl(delegate);
    }

    @Test
    void shouldDelegateAdd(@Random Long encounterId, @Random AddParticipantRequest request, @Random ParticipantViewModel expected) {
        given(delegate.add(encounterId, request)).willReturn(expected);

        var result = sut.add(encounterId, request);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().add(encounterId, request);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}

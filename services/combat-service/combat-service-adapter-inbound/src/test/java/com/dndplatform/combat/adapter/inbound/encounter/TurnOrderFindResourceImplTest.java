package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.vm.ParticipantViewModel;
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
class TurnOrderFindResourceImplTest {

    @Mock
    private TurnOrderFindDelegate delegate;

    private TurnOrderFindResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new TurnOrderFindResourceImpl(delegate);
    }

    @Test
    void shouldDelegateGetTurnOrder(@Random Long encounterId, @Random ParticipantViewModel vm1, @Random ParticipantViewModel vm2) {
        var expected = List.of(vm1, vm2);
        given(delegate.getTurnOrder(encounterId)).willReturn(expected);

        var result = sut.getTurnOrder(encounterId);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().getTurnOrder(encounterId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}

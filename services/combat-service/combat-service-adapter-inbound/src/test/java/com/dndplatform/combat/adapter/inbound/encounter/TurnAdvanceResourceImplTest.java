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
class TurnAdvanceResourceImplTest {

    @Mock
    private TurnAdvanceDelegate delegate;

    private TurnAdvanceResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new TurnAdvanceResourceImpl(delegate);
    }

    @Test
    void shouldDelegateAdvance(@Random Long encounterId, @Random Long userId, @Random ParticipantViewModel vm1, @Random ParticipantViewModel vm2) {
        var expected = List.of(vm1, vm2);
        given(delegate.advance(encounterId, userId)).willReturn(expected);

        var result = sut.advance(encounterId, userId);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().advance(encounterId, userId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}

package com.dndplatform.combat.adapter.inbound.encounter;

import com.dndplatform.combat.view.model.vm.DifficultyResultViewModel;
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
class DifficultyCalculateResourceImplTest {

    @Mock
    private DifficultyCalculateDelegate delegate;

    private DifficultyCalculateResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DifficultyCalculateResourceImpl(delegate);
    }

    @Test
    void shouldDelegateCalculate(@Random Long encounterId, @Random DifficultyResultViewModel expected) {
        given(delegate.calculate(encounterId)).willReturn(expected);

        var result = sut.calculate(encounterId);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().calculate(encounterId);
        then(delegate).shouldHaveNoMoreInteractions();
    }
}

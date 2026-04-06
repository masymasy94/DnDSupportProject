package com.dndplatform.compendium.adapter.inbound.conditions.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.ConditionFindAllResource;
import com.dndplatform.compendium.view.model.vm.ConditionViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ConditionFindAllResourceImplTest {
    @Mock private ConditionFindAllResource delegate;
    private ConditionFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new ConditionFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random ConditionViewModel vm1, @Random ConditionViewModel vm2) {
        List<ConditionViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<ConditionViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}

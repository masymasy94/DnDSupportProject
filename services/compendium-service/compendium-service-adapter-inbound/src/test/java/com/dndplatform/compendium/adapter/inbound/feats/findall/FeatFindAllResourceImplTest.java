package com.dndplatform.compendium.adapter.inbound.feats.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.FeatFindAllResource;
import com.dndplatform.compendium.view.model.vm.FeatViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class FeatFindAllResourceImplTest {
    @Mock private FeatFindAllResource delegate;
    private FeatFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new FeatFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random FeatViewModel vm1, @Random FeatViewModel vm2) {
        List<FeatViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<FeatViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}

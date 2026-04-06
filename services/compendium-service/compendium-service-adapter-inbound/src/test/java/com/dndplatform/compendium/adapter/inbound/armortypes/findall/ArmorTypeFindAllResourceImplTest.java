package com.dndplatform.compendium.adapter.inbound.armortypes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.ArmorTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.ArmorTypeViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ArmorTypeFindAllResourceImplTest {
    @Mock private ArmorTypeFindAllResource delegate;
    private ArmorTypeFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new ArmorTypeFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random ArmorTypeViewModel vm1, @Random ArmorTypeViewModel vm2) {
        List<ArmorTypeViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<ArmorTypeViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}

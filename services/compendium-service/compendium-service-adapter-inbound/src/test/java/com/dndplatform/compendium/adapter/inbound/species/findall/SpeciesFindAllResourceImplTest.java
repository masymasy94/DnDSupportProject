package com.dndplatform.compendium.adapter.inbound.species.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.SpeciesFindAllResource;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class SpeciesFindAllResourceImplTest {
    @Mock private SpeciesFindAllResource delegate;
    private SpeciesFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new SpeciesFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random SpeciesViewModel vm1, @Random SpeciesViewModel vm2) {
        List<SpeciesViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<SpeciesViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}

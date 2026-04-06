package com.dndplatform.compendium.adapter.inbound.alignments.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.AlignmentFindAllResource;
import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class AlignmentFindAllResourceImplTest {
    @Mock private AlignmentFindAllResource delegate;
    private AlignmentFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new AlignmentFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random AlignmentViewModel vm1, @Random AlignmentViewModel vm2) {
        List<AlignmentViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<AlignmentViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}

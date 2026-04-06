package com.dndplatform.compendium.adapter.inbound.tooltypes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.ToolTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.ToolTypeViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ToolTypeFindAllResourceImplTest {
    @Mock private ToolTypeFindAllResource delegate;
    private ToolTypeFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new ToolTypeFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random ToolTypeViewModel vm1, @Random ToolTypeViewModel vm2) {
        String category = "ARTISAN";
        List<ToolTypeViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll(category)).willReturn(expected);
        List<ToolTypeViewModel> result = sut.findAll(category);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll(category);
        inOrder.verifyNoMoreInteractions();
    }
}

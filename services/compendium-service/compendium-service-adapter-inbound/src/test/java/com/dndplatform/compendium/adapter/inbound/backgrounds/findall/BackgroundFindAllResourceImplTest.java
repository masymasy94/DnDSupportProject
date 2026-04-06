package com.dndplatform.compendium.adapter.inbound.backgrounds.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.BackgroundFindAllResource;
import com.dndplatform.compendium.view.model.vm.BackgroundViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class BackgroundFindAllResourceImplTest {
    @Mock private BackgroundFindAllResource delegate;
    private BackgroundFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new BackgroundFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random BackgroundViewModel vm1, @Random BackgroundViewModel vm2) {
        List<BackgroundViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll()).willReturn(expected);
        List<BackgroundViewModel> result = sut.findAll();
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll();
        inOrder.verifyNoMoreInteractions();
    }
}

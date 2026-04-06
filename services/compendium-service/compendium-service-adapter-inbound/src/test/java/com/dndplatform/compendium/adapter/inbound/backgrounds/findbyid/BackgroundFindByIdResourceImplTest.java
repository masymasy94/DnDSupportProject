package com.dndplatform.compendium.adapter.inbound.backgrounds.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.BackgroundFindByIdResource;
import com.dndplatform.compendium.view.model.vm.BackgroundViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class BackgroundFindByIdResourceImplTest {
    @Mock private BackgroundFindByIdResource delegate;
    private BackgroundFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new BackgroundFindByIdResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random BackgroundViewModel expected) {
        int id = 1;
        given(delegate.findById(id)).willReturn(expected);
        BackgroundViewModel result = sut.findById(id);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}

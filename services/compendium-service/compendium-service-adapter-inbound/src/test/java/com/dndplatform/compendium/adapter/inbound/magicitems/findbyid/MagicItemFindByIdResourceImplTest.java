package com.dndplatform.compendium.adapter.inbound.magicitems.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.MagicItemFindByIdResource;
import com.dndplatform.compendium.view.model.vm.MagicItemViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class MagicItemFindByIdResourceImplTest {
    @Mock private MagicItemFindByIdResource delegate;
    private MagicItemFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new MagicItemFindByIdResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random MagicItemViewModel expected) {
        int id = 1;
        given(delegate.findById(id)).willReturn(expected);
        MagicItemViewModel result = sut.findById(id);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}

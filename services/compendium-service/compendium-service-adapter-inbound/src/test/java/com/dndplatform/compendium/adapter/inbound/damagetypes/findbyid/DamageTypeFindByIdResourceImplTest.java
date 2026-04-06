package com.dndplatform.compendium.adapter.inbound.damagetypes.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.DamageTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.DamageTypeViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DamageTypeFindByIdResourceImplTest {
    @Mock private DamageTypeFindByIdResource delegate;
    private DamageTypeFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new DamageTypeFindByIdResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random DamageTypeViewModel expected) {
        int id = 1;
        given(delegate.findById(id)).willReturn(expected);
        DamageTypeViewModel result = sut.findById(id);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}

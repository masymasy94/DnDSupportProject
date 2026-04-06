package com.dndplatform.compendium.adapter.inbound.weapontypes.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.WeaponTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class WeaponTypeFindByIdResourceImplTest {
    @Mock private WeaponTypeFindByIdResource delegate;
    private WeaponTypeFindByIdResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new WeaponTypeFindByIdResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random WeaponTypeViewModel expected) {
        int id = 1;
        given(delegate.findById(id)).willReturn(expected);
        WeaponTypeViewModel result = sut.findById(id);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findById(id);
        inOrder.verifyNoMoreInteractions();
    }
}

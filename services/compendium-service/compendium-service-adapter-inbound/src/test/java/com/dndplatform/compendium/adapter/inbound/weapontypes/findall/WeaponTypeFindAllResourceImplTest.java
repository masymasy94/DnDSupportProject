package com.dndplatform.compendium.adapter.inbound.weapontypes.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.WeaponTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class WeaponTypeFindAllResourceImplTest {
    @Mock private WeaponTypeFindAllResource delegate;
    private WeaponTypeFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new WeaponTypeFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random WeaponTypeViewModel vm1, @Random WeaponTypeViewModel vm2) {
        String category = "MARTIAL";
        List<WeaponTypeViewModel> expected = List.of(vm1, vm2);
        given(delegate.findAll(category)).willReturn(expected);
        List<WeaponTypeViewModel> result = sut.findAll(category);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll(category);
        inOrder.verifyNoMoreInteractions();
    }
}

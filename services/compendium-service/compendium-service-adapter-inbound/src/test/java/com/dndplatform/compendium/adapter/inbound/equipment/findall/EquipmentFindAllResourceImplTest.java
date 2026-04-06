package com.dndplatform.compendium.adapter.inbound.equipment.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.view.model.EquipmentFindAllResource;
import com.dndplatform.compendium.view.model.vm.PagedEquipmentViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EquipmentFindAllResourceImplTest {
    @Mock private EquipmentFindAllResource delegate;
    private EquipmentFindAllResourceImpl sut;

    @BeforeEach
    void setUp() { sut = new EquipmentFindAllResourceImpl(delegate); }

    @Test
    void shouldDelegateToDelegate(@Random PagedEquipmentViewModel expected) {
        String name = "sword";
        String category = "Martial Weapons";
        Integer page = 0;
        Integer pageSize = 50;
        given(delegate.findAll(name, category, page, pageSize)).willReturn(expected);
        PagedEquipmentViewModel result = sut.findAll(name, category, page, pageSize);
        assertThat(result).isEqualTo(expected);
        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).findAll(name, category, page, pageSize);
        inOrder.verifyNoMoreInteractions();
    }
}

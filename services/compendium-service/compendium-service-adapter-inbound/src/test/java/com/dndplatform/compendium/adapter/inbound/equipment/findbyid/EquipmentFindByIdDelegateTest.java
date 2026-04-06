package com.dndplatform.compendium.adapter.inbound.equipment.findbyid;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.equipment.findall.mapper.EquipmentViewModelMapper;
import com.dndplatform.compendium.domain.EquipmentFindByIdService;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.view.model.vm.EquipmentViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EquipmentFindByIdDelegateTest {

    @Mock
    private EquipmentFindByIdService service;

    @Mock
    private EquipmentViewModelMapper mapper;

    private EquipmentFindByIdDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new EquipmentFindByIdDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToServiceAndMapper(@Random Equipment equipment, @Random EquipmentViewModel expected) {
        given(service.findById(equipment.id())).willReturn(equipment);
        given(mapper.apply(equipment)).willReturn(expected);

        EquipmentViewModel result = sut.findById(equipment.id());

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(service, mapper);
        then(service).should(inOrder).findById(equipment.id());
        then(mapper).should(inOrder).apply(equipment);
        inOrder.verifyNoMoreInteractions();
    }
}

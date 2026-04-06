package com.dndplatform.compendium.adapter.inbound.equipment.findall;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.inbound.equipment.findall.mapper.EquipmentViewModelMapper;
import com.dndplatform.compendium.domain.EquipmentFindAllService;
import com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.view.model.vm.EquipmentViewModel;
import com.dndplatform.compendium.view.model.vm.PagedEquipmentViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EquipmentFindAllDelegateTest {

    @Mock
    private EquipmentFindAllService service;

    @Mock
    private EquipmentViewModelMapper mapper;

    private EquipmentFindAllDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new EquipmentFindAllDelegate(service, mapper);
    }

    @Test
    void shouldDelegateToService(@Random Equipment e1, @Random Equipment e2,
                                 @Random EquipmentViewModel vm1, @Random EquipmentViewModel vm2) {
        var pagedResult = new PagedResult<>(List.of(e1, e2), 0, 50, 2L);
        given(mapper.apply(e1)).willReturn(vm1);
        given(mapper.apply(e2)).willReturn(vm2);

        ArgumentCaptor<EquipmentFilterCriteria> criteriaCaptor = ArgumentCaptor.forClass(EquipmentFilterCriteria.class);
        given(service.findAll(criteriaCaptor.capture())).willReturn(pagedResult);

        PagedEquipmentViewModel result = sut.findAll("sword", "weapon", 0, 50);

        assertThat(result.content()).containsExactly(vm1, vm2);
        assertThat(result.page()).isEqualTo(0);
        assertThat(result.size()).isEqualTo(50);
        assertThat(result.totalElements()).isEqualTo(2L);

        EquipmentFilterCriteria criteria = criteriaCaptor.getValue();
        assertThat(criteria.name()).isEqualTo("sword");
        assertThat(criteria.category()).isEqualTo("weapon");
        assertThat(criteria.page()).isEqualTo(0);
        assertThat(criteria.pageSize()).isEqualTo(50);

        then(service).should().findAll(criteria);
        then(mapper).should().apply(e1);
        then(mapper).should().apply(e2);
    }
}

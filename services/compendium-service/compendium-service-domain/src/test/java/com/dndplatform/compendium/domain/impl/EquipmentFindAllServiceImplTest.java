package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.EquipmentFindAllService;
import com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.EquipmentFindAllRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class EquipmentFindAllServiceImplTest {

    @Mock
    private EquipmentFindAllRepository repository;

    private EquipmentFindAllService sut;

    @BeforeEach
    void setUp() {
        sut = new EquipmentFindAllServiceImpl(repository);
    }

    @Test
    void shouldFindAll(@Random Equipment equipment) {
        EquipmentFilterCriteria criteria = new EquipmentFilterCriteria(null, null, 0, 20);
        List<Equipment> items = List.of(equipment);
        PagedResult<Equipment> expected = new PagedResult<>(items, 0, 20, 1);
        given(repository.findAllEquipment(criteria)).willReturn(expected);

        PagedResult<Equipment> result = sut.findAll(criteria);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(repository);
        then(repository).should(inOrder).findAllEquipment(criteria);
        inOrder.verifyNoMoreInteractions();
    }
}

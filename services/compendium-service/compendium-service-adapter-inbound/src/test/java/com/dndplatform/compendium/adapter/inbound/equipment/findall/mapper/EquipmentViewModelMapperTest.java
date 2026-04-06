package com.dndplatform.compendium.adapter.inbound.equipment.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.view.model.vm.EquipmentViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class EquipmentViewModelMapperTest {

    private final EquipmentViewModelMapper sut = Mappers.getMapper(EquipmentViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random Equipment equipment) {
        EquipmentViewModel result = sut.apply(equipment);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(equipment.id());
        assertThat(result.name()).isEqualTo(equipment.name());
        assertThat(result.category()).isEqualTo(equipment.category());
        assertThat(result.description()).isEqualTo(equipment.description());
        assertThat(result.source()).isEqualTo(equipment.source().name());
    }
}

package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.EquipmentCategoryEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.EquipmentEntity;
import com.dndplatform.compendium.domain.model.SourceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class EquipmentMapperTest {

    private final EquipmentMapper sut = Mappers.getMapper(EquipmentMapper.class);

    @Test
    void shouldMapToEquipment(@Random EquipmentEntity entity) {
        var category = new EquipmentCategoryEntity();
        category.name = "Weapon";
        entity.equipmentCategory = category;
        entity.source = null;

        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.category()).isEqualTo("Weapon");
        assertThat(result.source()).isEqualTo(SourceType.OFFICIAL);
        assertThat(result.ownerId()).isEqualTo(entity.ownerId);
        assertThat(result.campaignId()).isEqualTo(entity.campaignId);
    }

    @Test
    void shouldDefaultIsPublicToFalseWhenNull(@Random EquipmentEntity entity) {
        var category = new EquipmentCategoryEntity();
        category.name = "Armor";
        entity.equipmentCategory = category;
        entity.source = null;
        entity.isPublic = null;

        var result = sut.apply(entity);

        assertThat(result.isPublic()).isFalse();
    }
}

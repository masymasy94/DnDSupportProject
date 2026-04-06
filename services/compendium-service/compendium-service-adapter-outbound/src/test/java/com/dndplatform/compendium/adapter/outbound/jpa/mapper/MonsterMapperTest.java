package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.MonsterEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.MonsterSizeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.MonsterTypeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class MonsterMapperTest {

    private final MonsterMapper sut = Mappers.getMapper(MonsterMapper.class);

    @Test
    void shouldMapToMonster(@Random MonsterEntity entity) {
        var size = new MonsterSizeEntity();
        size.name = "Large";
        entity.monsterSize = size;

        var type = new MonsterTypeEntity();
        type.name = "Beast";
        entity.monsterType = type;
        entity.source = "OFFICIAL";

        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.index()).isEqualTo(entity.index);
        assertThat(result.size()).isEqualTo("Large");
        assertThat(result.type()).isEqualTo("Beast");
        assertThat(result.armorClass()).isEqualTo(entity.armorClass);
        assertThat(result.hitPoints()).isEqualTo(entity.hitPoints);
    }

    @Test
    void shouldDefaultIsPublicToFalseWhenNull(@Random MonsterEntity entity) {
        var size = new MonsterSizeEntity();
        size.name = "Medium";
        entity.monsterSize = size;

        var type = new MonsterTypeEntity();
        type.name = "Humanoid";
        entity.monsterType = type;
        entity.source = "OFFICIAL";

        entity.isPublic = null;

        var result = sut.apply(entity);

        assertThat(result.isPublic()).isFalse();
    }
}

package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.ToolTypeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class ToolTypeMapperTest {

    private final ToolTypeMapper sut = Mappers.getMapper(ToolTypeMapper.class);

    @Test
    void shouldMapToToolType(@Random ToolTypeEntity entity) {
        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.category()).isEqualTo(entity.category);
    }
}

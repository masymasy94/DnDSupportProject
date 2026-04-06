package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.AlignmentEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class AlignmentMapperTest {

    private final AlignmentMapper sut = Mappers.getMapper(AlignmentMapper.class);

    @Test
    void shouldMapToAlignment(@Random AlignmentEntity entity) {
        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.code()).isEqualTo(entity.code);
        assertThat(result.name()).isEqualTo(entity.name);
    }
}

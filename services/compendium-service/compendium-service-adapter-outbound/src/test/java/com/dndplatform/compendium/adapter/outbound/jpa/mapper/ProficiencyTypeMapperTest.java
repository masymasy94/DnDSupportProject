package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.ProficiencyTypeEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class ProficiencyTypeMapperTest {

    private final ProficiencyTypeMapper sut = Mappers.getMapper(ProficiencyTypeMapper.class);

    @Test
    void shouldMapToProficiencyType(@Random ProficiencyTypeEntity entity) {
        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
    }
}

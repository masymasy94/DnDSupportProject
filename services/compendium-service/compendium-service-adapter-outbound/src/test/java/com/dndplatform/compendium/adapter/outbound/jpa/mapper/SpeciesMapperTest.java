package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpeciesEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class SpeciesMapperTest {

    private final SpeciesMapper sut = Mappers.getMapper(SpeciesMapper.class);

    @Test
    void shouldMapToSpecies(@Random SpeciesEntity entity) {
        entity.source = null;

        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.description()).isEqualTo(entity.description);
        assertThat(result.source()).isNull();
        assertThat(result.ownerId()).isEqualTo(entity.ownerId);
        assertThat(result.campaignId()).isEqualTo(entity.campaignId);
    }
}

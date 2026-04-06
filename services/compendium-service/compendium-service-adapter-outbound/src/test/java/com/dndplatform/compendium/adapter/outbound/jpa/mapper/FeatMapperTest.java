package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.FeatEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class FeatMapperTest {

    private final FeatMapper sut = Mappers.getMapper(FeatMapper.class);

    @Test
    void shouldMapToFeat(@Random FeatEntity entity) {
        entity.source = null;

        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.description()).isEqualTo(entity.description);
        assertThat(result.prerequisite()).isEqualTo(entity.prerequisite);
        assertThat(result.benefit()).isEqualTo(entity.benefit);
        assertThat(result.ownerId()).isEqualTo(entity.ownerId);
        assertThat(result.campaignId()).isEqualTo(entity.campaignId);
    }

    @Test
    void shouldDefaultIsPublicToFalseWhenNull(@Random FeatEntity entity) {
        entity.source = null;
        entity.isPublic = null;

        var result = sut.apply(entity);

        assertThat(result.isPublic()).isFalse();
    }

    @Test
    void shouldDefaultGrantsAbilityIncreaseToFalseWhenNull(@Random FeatEntity entity) {
        entity.source = null;
        entity.grantsAbilityIncrease = null;

        var result = sut.apply(entity);

        assertThat(result.grantsAbilityIncrease()).isFalse();
    }
}

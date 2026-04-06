package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.BackgroundEntity;
import com.dndplatform.compendium.domain.model.SourceType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class BackgroundMapperTest {

    private final BackgroundMapper sut = Mappers.getMapper(BackgroundMapper.class);

    @Test
    void shouldMapToBackground(@Random BackgroundEntity entity) {
        entity.source = null;

        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.description()).isEqualTo(entity.description);
        assertThat(result.source()).isEqualTo(SourceType.OFFICIAL);
        assertThat(result.ownerId()).isEqualTo(entity.ownerId);
        assertThat(result.campaignId()).isEqualTo(entity.campaignId);
    }

    @Test
    void shouldMapSourceToSpecifiedType(@Random BackgroundEntity entity) {
        entity.source = "HOMEBREW";

        var result = sut.apply(entity);

        assertThat(result.source()).isEqualTo(SourceType.HOMEBREW);
    }

    @Test
    void shouldDefaultIsPublicToFalseWhenNull(@Random BackgroundEntity entity) {
        entity.source = null;
        entity.isPublic = null;

        var result = sut.apply(entity);

        assertThat(result.isPublic()).isFalse();
    }
}

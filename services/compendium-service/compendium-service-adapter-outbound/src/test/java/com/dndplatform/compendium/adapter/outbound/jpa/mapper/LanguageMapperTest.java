package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.LanguageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class LanguageMapperTest {

    private final LanguageMapper sut = Mappers.getMapper(LanguageMapper.class);

    @Test
    void shouldMapToLanguage(@Random LanguageEntity entity) {
        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.script()).isEqualTo(entity.script);
        assertThat(result.type()).isEqualTo(entity.type);
    }
}

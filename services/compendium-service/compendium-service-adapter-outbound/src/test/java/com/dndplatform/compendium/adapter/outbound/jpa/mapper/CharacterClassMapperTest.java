package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.CharacterClassEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class CharacterClassMapperTest {

    private final CharacterClassMapper sut = Mappers.getMapper(CharacterClassMapper.class);

    @Test
    void shouldMapToCharacterClass(@Random CharacterClassEntity entity) {
        entity.source = null;

        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.hitDie()).isEqualTo(entity.hitDie);
        assertThat(result.description()).isEqualTo(entity.description);
        assertThat(result.source()).isNull();
        assertThat(result.ownerId()).isEqualTo(entity.ownerId);
        assertThat(result.campaignId()).isEqualTo(entity.campaignId);
    }
}

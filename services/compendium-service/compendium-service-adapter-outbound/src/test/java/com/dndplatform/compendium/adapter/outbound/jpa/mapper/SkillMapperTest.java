package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.AbilityEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.SkillEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class SkillMapperTest {

    private final SkillMapper sut = Mappers.getMapper(SkillMapper.class);

    @Test
    void shouldMapToSkill(@Random SkillEntity entity) {
        var ability = new AbilityEntity();
        ability.id = 1L;
        ability.name = "Strength";
        entity.ability = ability;

        var result = sut.apply(entity);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(entity.name);
        assertThat(result.abilityId()).isEqualTo((short) 1);
        assertThat(result.abilityName()).isEqualTo("Strength");
    }
}

package com.dndplatform.compendium.adapter.inbound.skills.findall.mapper;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.view.model.vm.SkillViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class SkillViewModelMapperTest {

    private final SkillViewModelMapper sut = Mappers.getMapper(SkillViewModelMapper.class);

    @Test
    void shouldMapToViewModel(@Random Skill skill) {
        SkillViewModel result = sut.apply(skill);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(skill.id());
        assertThat(result.name()).isEqualTo(skill.name());
        assertThat(result.abilityId()).isEqualTo(skill.abilityId());
        assertThat(result.abilityName()).isEqualTo(skill.abilityName());
    }
}

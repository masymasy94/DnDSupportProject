package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.SkillEntity;
import com.dndplatform.compendium.domain.model.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface SkillMapper extends Function<SkillEntity, Skill> {

    @Override
    @Mapping(target = "abilityId", source = "ability.id")
    @Mapping(target = "abilityName", source = "ability.name")
    Skill apply(SkillEntity entity);
}

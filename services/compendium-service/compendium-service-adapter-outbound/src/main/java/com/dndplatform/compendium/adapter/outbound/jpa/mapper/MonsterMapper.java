package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.MonsterEntity;
import com.dndplatform.compendium.domain.model.Monster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface MonsterMapper extends Function<MonsterEntity, Monster> {

    @Override
    @Mapping(source = "monsterSize.name", target = "size")
    @Mapping(source = "monsterType.name", target = "type")
    @Mapping(source = "isPublic", target = "isPublic", defaultValue = "false")
    Monster apply(MonsterEntity entity);
}

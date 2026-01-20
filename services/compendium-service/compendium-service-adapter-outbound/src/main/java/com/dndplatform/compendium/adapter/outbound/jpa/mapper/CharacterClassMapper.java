package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.CharacterClassEntity;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.domain.model.SourceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface CharacterClassMapper extends Function<CharacterClassEntity, CharacterClass> {

    @Override
    CharacterClass apply(CharacterClassEntity entity);
}

package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpellEntity;
import com.dndplatform.compendium.domain.model.Spell;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface SpellMapper extends Function<SpellEntity, Spell> {

    @Override
    @Mapping(source = "spellSchool.name", target = "school")
    @Mapping(source = "concentration", target = "concentration", defaultValue = "false")
    @Mapping(source = "ritual", target = "ritual", defaultValue = "false")
    @Mapping(source = "isPublic", target = "isPublic", defaultValue = "false")
    Spell apply(SpellEntity entity);
}

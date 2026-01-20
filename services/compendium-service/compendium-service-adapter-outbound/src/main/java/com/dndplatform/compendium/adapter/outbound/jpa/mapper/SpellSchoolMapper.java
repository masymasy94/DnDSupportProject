package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpellSchoolEntity;
import com.dndplatform.compendium.domain.model.SpellSchool;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface SpellSchoolMapper extends Function<SpellSchoolEntity, SpellSchool> {

    @Override
    SpellSchool apply(SpellSchoolEntity entity);
}

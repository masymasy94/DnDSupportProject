package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.DamageTypeEntity;
import com.dndplatform.compendium.domain.model.DamageType;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface DamageTypeMapper extends Function<DamageTypeEntity, DamageType> {

    @Override
    DamageType apply(DamageTypeEntity entity);
}

package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ArmorTypeEntity;
import com.dndplatform.compendium.domain.model.ArmorType;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface ArmorTypeMapper extends Function<ArmorTypeEntity, ArmorType> {

    @Override
    ArmorType apply(ArmorTypeEntity entity);
}

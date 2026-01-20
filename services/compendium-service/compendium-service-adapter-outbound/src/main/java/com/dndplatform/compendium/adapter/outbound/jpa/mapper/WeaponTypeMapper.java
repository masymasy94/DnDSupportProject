package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.WeaponTypeEntity;
import com.dndplatform.compendium.domain.model.WeaponType;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface WeaponTypeMapper extends Function<WeaponTypeEntity, WeaponType> {

    @Override
    WeaponType apply(WeaponTypeEntity entity);
}

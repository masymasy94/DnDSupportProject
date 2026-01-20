package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.FeatEntity;
import com.dndplatform.compendium.domain.model.Feat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface FeatMapper extends Function<FeatEntity, Feat> {

    @Override
    @Mapping(source = "isPublic", target = "isPublic", defaultValue = "false")
    @Mapping(source = "grantsAbilityIncrease", target = "grantsAbilityIncrease", defaultValue = "false")
    Feat apply(FeatEntity entity);
}

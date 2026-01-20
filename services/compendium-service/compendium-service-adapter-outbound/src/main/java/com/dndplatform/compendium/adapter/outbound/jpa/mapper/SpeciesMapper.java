package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpeciesEntity;
import com.dndplatform.compendium.domain.model.Species;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface SpeciesMapper extends Function<SpeciesEntity, Species> {

    @Override
    Species apply(SpeciesEntity entity);

}

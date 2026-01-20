package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ProficiencyTypeEntity;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface ProficiencyTypeMapper extends Function<ProficiencyTypeEntity, ProficiencyType> {

    @Override
    ProficiencyType apply(ProficiencyTypeEntity entity);
}

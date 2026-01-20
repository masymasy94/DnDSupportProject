package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ToolTypeEntity;
import com.dndplatform.compendium.domain.model.ToolType;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface ToolTypeMapper extends Function<ToolTypeEntity, ToolType> {

    @Override
    ToolType apply(ToolTypeEntity entity);
}

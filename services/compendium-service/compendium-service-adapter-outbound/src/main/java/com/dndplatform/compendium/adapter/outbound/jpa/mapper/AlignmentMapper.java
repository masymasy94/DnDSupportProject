package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.AlignmentEntity;
import com.dndplatform.compendium.domain.model.Alignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface AlignmentMapper extends Function<AlignmentEntity, Alignment> {

    @Override
    Alignment apply(AlignmentEntity entity);


}

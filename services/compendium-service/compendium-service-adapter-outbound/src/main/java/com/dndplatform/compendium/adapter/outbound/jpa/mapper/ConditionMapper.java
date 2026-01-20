package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ConditionEntity;
import com.dndplatform.compendium.domain.model.Condition;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface ConditionMapper extends Function<ConditionEntity, Condition> {

    @Override
    Condition apply(ConditionEntity entity);
}

package com.dndplatform.compendium.adapter.inbound.conditions.findall.mapper;

import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.view.model.vm.ConditionViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface ConditionViewModelMapper extends Function<Condition, ConditionViewModel> {

    @Override
    ConditionViewModel apply(Condition condition);
}

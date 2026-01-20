package com.dndplatform.compendium.adapter.inbound.tooltypes.findall.mapper;

import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.view.model.vm.ToolTypeViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface ToolTypeViewModelMapper extends Function<ToolType, ToolTypeViewModel> {

    @Override
    ToolTypeViewModel apply(ToolType toolType);
}

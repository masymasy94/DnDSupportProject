package com.dndplatform.compendium.adapter.inbound.armortypes.findall.mapper;

import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.view.model.vm.ArmorTypeViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface ArmorTypeViewModelMapper extends Function<ArmorType, ArmorTypeViewModel> {

    @Override
    ArmorTypeViewModel apply(ArmorType armorType);
}

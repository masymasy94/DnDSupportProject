package com.dndplatform.compendium.adapter.inbound.damagetypes.findall.mapper;

import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.view.model.vm.DamageTypeViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface DamageTypeViewModelMapper extends Function<DamageType, DamageTypeViewModel> {

    @Override
    DamageTypeViewModel apply(DamageType damageType);
}

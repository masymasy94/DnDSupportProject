package com.dndplatform.compendium.adapter.inbound.weapontypes.findall.mapper;

import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface WeaponTypeViewModelMapper extends Function<WeaponType, WeaponTypeViewModel> {

    @Override
    WeaponTypeViewModel apply(WeaponType weaponType);
}

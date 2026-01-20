package com.dndplatform.compendium.adapter.inbound.monsters.findall.mapper;

import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.view.model.vm.MonsterViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface MonsterViewModelMapper extends Function<Monster, MonsterViewModel> {

    @Override
    MonsterViewModel apply(Monster monster);
}

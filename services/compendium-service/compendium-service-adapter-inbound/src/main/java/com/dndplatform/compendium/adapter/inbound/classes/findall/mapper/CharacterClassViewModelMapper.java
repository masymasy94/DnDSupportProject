package com.dndplatform.compendium.adapter.inbound.classes.findall.mapper;

import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.view.model.vm.CharacterClassViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface CharacterClassViewModelMapper extends Function<CharacterClass, CharacterClassViewModel> {

    @Override
    CharacterClassViewModel apply(CharacterClass characterClass);
}

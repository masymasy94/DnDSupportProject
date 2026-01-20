package com.dndplatform.compendium.adapter.inbound.spells.findall.mapper;

import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.view.model.vm.SpellViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface SpellViewModelMapper extends Function<Spell, SpellViewModel> {

    @Override
    SpellViewModel apply(Spell spell);
}

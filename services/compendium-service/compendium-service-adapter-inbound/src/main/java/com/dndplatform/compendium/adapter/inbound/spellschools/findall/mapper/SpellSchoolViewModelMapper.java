package com.dndplatform.compendium.adapter.inbound.spellschools.findall.mapper;

import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.view.model.vm.SpellSchoolViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface SpellSchoolViewModelMapper extends Function<SpellSchool, SpellSchoolViewModel> {

    @Override
    SpellSchoolViewModel apply(SpellSchool spellSchool);
}

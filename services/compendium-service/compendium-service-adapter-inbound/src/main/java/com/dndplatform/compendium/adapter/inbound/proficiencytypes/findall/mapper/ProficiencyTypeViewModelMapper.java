package com.dndplatform.compendium.adapter.inbound.proficiencytypes.findall.mapper;

import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.view.model.vm.ProficiencyTypeViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface ProficiencyTypeViewModelMapper extends Function<ProficiencyType, ProficiencyTypeViewModel> {

    @Override
    ProficiencyTypeViewModel apply(ProficiencyType proficiencyType);
}

package com.dndplatform.compendium.adapter.inbound.species.findall.mapper;

import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface SpeciesViewModelMapper extends Function<Species, SpeciesViewModel> {

    @Override
    SpeciesViewModel apply(Species species);
}

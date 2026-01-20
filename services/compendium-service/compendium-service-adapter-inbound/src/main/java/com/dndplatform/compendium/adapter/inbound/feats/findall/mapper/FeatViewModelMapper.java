package com.dndplatform.compendium.adapter.inbound.feats.findall.mapper;

import com.dndplatform.compendium.domain.model.Feat;
import com.dndplatform.compendium.view.model.vm.FeatViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface FeatViewModelMapper extends Function<Feat, FeatViewModel> {

    @Override
    FeatViewModel apply(Feat feat);
}

package com.dndplatform.compendium.adapter.inbound.magicitems.findall.mapper;

import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.view.model.vm.MagicItemViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface MagicItemViewModelMapper extends Function<MagicItem, MagicItemViewModel> {

    @Override
    @Mapping(target = "source", expression = "java(magicItem.source() != null ? magicItem.source().name() : null)")
    MagicItemViewModel apply(MagicItem magicItem);
}

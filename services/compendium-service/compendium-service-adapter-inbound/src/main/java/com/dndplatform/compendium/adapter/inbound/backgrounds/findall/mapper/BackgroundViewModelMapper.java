package com.dndplatform.compendium.adapter.inbound.backgrounds.findall.mapper;

import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.view.model.vm.BackgroundViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface BackgroundViewModelMapper extends Function<Background, BackgroundViewModel> {

    @Override
    @Mapping(target = "source", expression = "java(background.source() != null ? background.source().name() : null)")
    BackgroundViewModel apply(Background background);
}

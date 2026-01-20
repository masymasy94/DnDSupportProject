package com.dndplatform.compendium.adapter.inbound.alignments.findall.mapper;

import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
import org.mapstruct.Mapper;

import java.util.function.Function;

@Mapper
public interface AlignmentViewModelMapper extends Function<Alignment, AlignmentViewModel> {

    @Override
    AlignmentViewModel apply(Alignment alignment);
}

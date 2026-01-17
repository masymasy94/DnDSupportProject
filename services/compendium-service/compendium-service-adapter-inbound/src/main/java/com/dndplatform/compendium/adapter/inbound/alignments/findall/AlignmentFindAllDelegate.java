package com.dndplatform.compendium.adapter.inbound.alignments.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.alignments.findall.mapper.AlignmentViewModelMapper;
import com.dndplatform.compendium.domain.AlignmentFindAllService;
import com.dndplatform.compendium.view.model.AlignmentFindAllResource;
import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class AlignmentFindAllDelegate implements AlignmentFindAllResource {

    private final AlignmentFindAllService service;
    private final AlignmentViewModelMapper mapper;

    @Inject
    public AlignmentFindAllDelegate(AlignmentFindAllService service,
                                    AlignmentViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<AlignmentViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}

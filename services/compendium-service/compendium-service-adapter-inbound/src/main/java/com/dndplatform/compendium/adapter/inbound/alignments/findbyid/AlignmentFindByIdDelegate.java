package com.dndplatform.compendium.adapter.inbound.alignments.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.alignments.findall.mapper.AlignmentViewModelMapper;
import com.dndplatform.compendium.domain.AlignmentFindByIdService;
import com.dndplatform.compendium.view.model.AlignmentFindByIdResource;
import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class AlignmentFindByIdDelegate implements AlignmentFindByIdResource {

    private final AlignmentFindByIdService service;
    private final AlignmentViewModelMapper mapper;

    @Inject
    public AlignmentFindByIdDelegate(AlignmentFindByIdService service,
                                     AlignmentViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public AlignmentViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

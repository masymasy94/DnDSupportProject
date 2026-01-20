package com.dndplatform.compendium.adapter.inbound.tooltypes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.tooltypes.findall.mapper.ToolTypeViewModelMapper;
import com.dndplatform.compendium.domain.ToolTypeFindByIdService;
import com.dndplatform.compendium.view.model.ToolTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.ToolTypeViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class ToolTypeFindByIdDelegate implements ToolTypeFindByIdResource {

    private final ToolTypeFindByIdService service;
    private final ToolTypeViewModelMapper mapper;

    @Inject
    public ToolTypeFindByIdDelegate(ToolTypeFindByIdService service,
                                    ToolTypeViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ToolTypeViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

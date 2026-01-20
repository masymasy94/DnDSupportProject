package com.dndplatform.compendium.adapter.inbound.tooltypes.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.tooltypes.findall.mapper.ToolTypeViewModelMapper;
import com.dndplatform.compendium.domain.ToolTypeFindAllService;
import com.dndplatform.compendium.view.model.ToolTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.ToolTypeViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class ToolTypeFindAllDelegate implements ToolTypeFindAllResource {

    private final ToolTypeFindAllService service;
    private final ToolTypeViewModelMapper mapper;

    @Inject
    public ToolTypeFindAllDelegate(ToolTypeFindAllService service,
                                   ToolTypeViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<ToolTypeViewModel> findAll(String category) {
        return service.findAll(category).stream()
                .map(mapper)
                .toList();
    }
}

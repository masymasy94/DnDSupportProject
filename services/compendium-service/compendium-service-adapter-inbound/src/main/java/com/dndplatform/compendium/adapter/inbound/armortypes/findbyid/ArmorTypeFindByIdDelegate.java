package com.dndplatform.compendium.adapter.inbound.armortypes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.armortypes.findall.mapper.ArmorTypeViewModelMapper;
import com.dndplatform.compendium.domain.ArmorTypeFindByIdService;
import com.dndplatform.compendium.view.model.ArmorTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.ArmorTypeViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class ArmorTypeFindByIdDelegate implements ArmorTypeFindByIdResource {

    private final ArmorTypeFindByIdService service;
    private final ArmorTypeViewModelMapper mapper;

    @Inject
    public ArmorTypeFindByIdDelegate(ArmorTypeFindByIdService service,
                                     ArmorTypeViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ArmorTypeViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

package com.dndplatform.compendium.adapter.inbound.damagetypes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.damagetypes.findall.mapper.DamageTypeViewModelMapper;
import com.dndplatform.compendium.domain.DamageTypeFindByIdService;
import com.dndplatform.compendium.view.model.DamageTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.DamageTypeViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class DamageTypeFindByIdDelegate implements DamageTypeFindByIdResource {

    private final DamageTypeFindByIdService service;
    private final DamageTypeViewModelMapper mapper;

    @Inject
    public DamageTypeFindByIdDelegate(DamageTypeFindByIdService service,
                                      DamageTypeViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public DamageTypeViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

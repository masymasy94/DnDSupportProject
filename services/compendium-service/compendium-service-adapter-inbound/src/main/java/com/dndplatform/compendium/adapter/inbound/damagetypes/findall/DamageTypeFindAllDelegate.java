package com.dndplatform.compendium.adapter.inbound.damagetypes.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.damagetypes.findall.mapper.DamageTypeViewModelMapper;
import com.dndplatform.compendium.domain.DamageTypeFindAllService;
import com.dndplatform.compendium.view.model.DamageTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.DamageTypeViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class DamageTypeFindAllDelegate implements DamageTypeFindAllResource {

    private final DamageTypeFindAllService service;
    private final DamageTypeViewModelMapper mapper;

    @Inject
    public DamageTypeFindAllDelegate(DamageTypeFindAllService service,
                                     DamageTypeViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<DamageTypeViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}

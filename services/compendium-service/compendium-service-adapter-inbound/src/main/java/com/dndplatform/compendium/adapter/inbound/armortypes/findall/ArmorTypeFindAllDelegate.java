package com.dndplatform.compendium.adapter.inbound.armortypes.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.armortypes.findall.mapper.ArmorTypeViewModelMapper;
import com.dndplatform.compendium.domain.ArmorTypeFindAllService;
import com.dndplatform.compendium.view.model.ArmorTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.ArmorTypeViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class ArmorTypeFindAllDelegate implements ArmorTypeFindAllResource {

    private final ArmorTypeFindAllService service;
    private final ArmorTypeViewModelMapper mapper;

    @Inject
    public ArmorTypeFindAllDelegate(ArmorTypeFindAllService service,
                                    ArmorTypeViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<ArmorTypeViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}

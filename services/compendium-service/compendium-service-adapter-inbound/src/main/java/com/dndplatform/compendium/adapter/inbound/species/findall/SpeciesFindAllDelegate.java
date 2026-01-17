package com.dndplatform.compendium.adapter.inbound.species.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.species.findall.mapper.SpeciesViewModelMapper;
import com.dndplatform.compendium.domain.SpeciesFindAllService;
import com.dndplatform.compendium.view.model.SpeciesFindAllResource;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class SpeciesFindAllDelegate implements SpeciesFindAllResource {

    private final SpeciesFindAllService service;
    private final SpeciesViewModelMapper mapper;

    @Inject
    public SpeciesFindAllDelegate(SpeciesFindAllService service,
                                  SpeciesViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<SpeciesViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}

package com.dndplatform.compendium.adapter.inbound.proficiencytypes.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.proficiencytypes.findall.mapper.ProficiencyTypeViewModelMapper;
import com.dndplatform.compendium.domain.ProficiencyTypeFindAllService;
import com.dndplatform.compendium.view.model.ProficiencyTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.ProficiencyTypeViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class ProficiencyTypeFindAllDelegate implements ProficiencyTypeFindAllResource {

    private final ProficiencyTypeFindAllService service;
    private final ProficiencyTypeViewModelMapper mapper;

    @Inject
    public ProficiencyTypeFindAllDelegate(ProficiencyTypeFindAllService service,
                                          ProficiencyTypeViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<ProficiencyTypeViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}

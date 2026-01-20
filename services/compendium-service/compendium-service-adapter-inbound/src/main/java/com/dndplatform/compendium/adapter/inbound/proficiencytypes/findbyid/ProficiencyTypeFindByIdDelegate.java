package com.dndplatform.compendium.adapter.inbound.proficiencytypes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.proficiencytypes.findall.mapper.ProficiencyTypeViewModelMapper;
import com.dndplatform.compendium.domain.ProficiencyTypeFindByIdService;
import com.dndplatform.compendium.view.model.ProficiencyTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.ProficiencyTypeViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class ProficiencyTypeFindByIdDelegate implements ProficiencyTypeFindByIdResource {

    private final ProficiencyTypeFindByIdService service;
    private final ProficiencyTypeViewModelMapper mapper;

    @Inject
    public ProficiencyTypeFindByIdDelegate(ProficiencyTypeFindByIdService service,
                                           ProficiencyTypeViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ProficiencyTypeViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

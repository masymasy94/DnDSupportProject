package com.dndplatform.compendium.adapter.inbound.spellschools.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.spellschools.findall.mapper.SpellSchoolViewModelMapper;
import com.dndplatform.compendium.domain.SpellSchoolFindByIdService;
import com.dndplatform.compendium.view.model.SpellSchoolFindByIdResource;
import com.dndplatform.compendium.view.model.vm.SpellSchoolViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class SpellSchoolFindByIdDelegate implements SpellSchoolFindByIdResource {

    private final SpellSchoolFindByIdService service;
    private final SpellSchoolViewModelMapper mapper;

    @Inject
    public SpellSchoolFindByIdDelegate(SpellSchoolFindByIdService service,
                                       SpellSchoolViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public SpellSchoolViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

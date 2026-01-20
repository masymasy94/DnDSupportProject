package com.dndplatform.compendium.adapter.inbound.spellschools.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.spellschools.findall.mapper.SpellSchoolViewModelMapper;
import com.dndplatform.compendium.domain.SpellSchoolFindAllService;
import com.dndplatform.compendium.view.model.SpellSchoolFindAllResource;
import com.dndplatform.compendium.view.model.vm.SpellSchoolViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class SpellSchoolFindAllDelegate implements SpellSchoolFindAllResource {

    private final SpellSchoolFindAllService service;
    private final SpellSchoolViewModelMapper mapper;

    @Inject
    public SpellSchoolFindAllDelegate(SpellSchoolFindAllService service,
                                      SpellSchoolViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<SpellSchoolViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}

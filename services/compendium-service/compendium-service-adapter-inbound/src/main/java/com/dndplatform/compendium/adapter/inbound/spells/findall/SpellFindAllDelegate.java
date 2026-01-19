package com.dndplatform.compendium.adapter.inbound.spells.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.spells.findall.mapper.SpellViewModelMapper;
import com.dndplatform.compendium.domain.SpellFindAllService;
import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.view.model.SpellFindAllResource;
import com.dndplatform.compendium.view.model.vm.SpellViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class SpellFindAllDelegate implements SpellFindAllResource {

    private final SpellFindAllService service;
    private final SpellViewModelMapper mapper;

    @Inject
    public SpellFindAllDelegate(SpellFindAllService service,
                                SpellViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<SpellViewModel> findAll(Integer level, String school, Boolean concentration, Boolean ritual) {
        var criteria = new SpellFilterCriteria(level, school, concentration, ritual);
        return service.findAll(criteria).stream()
                .map(mapper)
                .toList();
    }
}

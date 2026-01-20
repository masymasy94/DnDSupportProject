package com.dndplatform.compendium.adapter.inbound.spells.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.spells.findall.mapper.SpellViewModelMapper;
import com.dndplatform.compendium.domain.SpellFindByIdService;
import com.dndplatform.compendium.view.model.SpellFindByIdResource;
import com.dndplatform.compendium.view.model.vm.SpellViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class SpellFindByIdDelegate implements SpellFindByIdResource {

    private final SpellFindByIdService service;
    private final SpellViewModelMapper mapper;

    @Inject
    public SpellFindByIdDelegate(SpellFindByIdService service,
                                 SpellViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public SpellViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

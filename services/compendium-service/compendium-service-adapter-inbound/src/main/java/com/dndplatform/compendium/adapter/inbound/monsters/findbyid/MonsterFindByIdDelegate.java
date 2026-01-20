package com.dndplatform.compendium.adapter.inbound.monsters.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.monsters.findall.mapper.MonsterViewModelMapper;
import com.dndplatform.compendium.domain.MonsterFindByIdService;
import com.dndplatform.compendium.view.model.MonsterFindByIdResource;
import com.dndplatform.compendium.view.model.vm.MonsterViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class MonsterFindByIdDelegate implements MonsterFindByIdResource {

    private final MonsterFindByIdService service;
    private final MonsterViewModelMapper mapper;

    @Inject
    public MonsterFindByIdDelegate(MonsterFindByIdService service,
                                   MonsterViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public MonsterViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

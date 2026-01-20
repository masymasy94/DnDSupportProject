package com.dndplatform.compendium.adapter.inbound.conditions.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.conditions.findall.mapper.ConditionViewModelMapper;
import com.dndplatform.compendium.domain.ConditionFindByIdService;
import com.dndplatform.compendium.view.model.ConditionFindByIdResource;
import com.dndplatform.compendium.view.model.vm.ConditionViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class ConditionFindByIdDelegate implements ConditionFindByIdResource {

    private final ConditionFindByIdService service;
    private final ConditionViewModelMapper mapper;

    @Inject
    public ConditionFindByIdDelegate(ConditionFindByIdService service,
                                     ConditionViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ConditionViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

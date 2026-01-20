package com.dndplatform.compendium.adapter.inbound.conditions.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.conditions.findall.mapper.ConditionViewModelMapper;
import com.dndplatform.compendium.domain.ConditionFindAllService;
import com.dndplatform.compendium.view.model.ConditionFindAllResource;
import com.dndplatform.compendium.view.model.vm.ConditionViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class ConditionFindAllDelegate implements ConditionFindAllResource {

    private final ConditionFindAllService service;
    private final ConditionViewModelMapper mapper;

    @Inject
    public ConditionFindAllDelegate(ConditionFindAllService service,
                                    ConditionViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<ConditionViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}

package com.dndplatform.compendium.adapter.inbound.backgrounds.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.backgrounds.findall.mapper.BackgroundViewModelMapper;
import com.dndplatform.compendium.domain.BackgroundFindByIdService;
import com.dndplatform.compendium.view.model.BackgroundFindByIdResource;
import com.dndplatform.compendium.view.model.vm.BackgroundViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class BackgroundFindByIdDelegate implements BackgroundFindByIdResource {

    private final BackgroundFindByIdService service;
    private final BackgroundViewModelMapper mapper;

    @Inject
    public BackgroundFindByIdDelegate(BackgroundFindByIdService service,
                                      BackgroundViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public BackgroundViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

package com.dndplatform.compendium.adapter.inbound.backgrounds.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.backgrounds.findall.mapper.BackgroundViewModelMapper;
import com.dndplatform.compendium.domain.BackgroundFindAllService;
import com.dndplatform.compendium.view.model.BackgroundFindAllResource;
import com.dndplatform.compendium.view.model.vm.BackgroundViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class BackgroundFindAllDelegate implements BackgroundFindAllResource {

    private final BackgroundFindAllService service;
    private final BackgroundViewModelMapper mapper;

    @Inject
    public BackgroundFindAllDelegate(BackgroundFindAllService service,
                                     BackgroundViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<BackgroundViewModel> findAll() {
        return service.findAll().stream()
                .map(mapper)
                .toList();
    }
}

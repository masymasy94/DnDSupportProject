package com.dndplatform.compendium.adapter.inbound.feats.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.feats.findall.mapper.FeatViewModelMapper;
import com.dndplatform.compendium.domain.FeatFindAllService;
import com.dndplatform.compendium.view.model.FeatFindAllResource;
import com.dndplatform.compendium.view.model.vm.FeatViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class FeatFindAllDelegate implements FeatFindAllResource {

    private final FeatFindAllService service;
    private final FeatViewModelMapper mapper;

    @Inject
    public FeatFindAllDelegate(FeatFindAllService service,
                               FeatViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<FeatViewModel> findAll() {
        return service.findAll().stream().map(mapper).toList();
    }
}

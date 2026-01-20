package com.dndplatform.compendium.adapter.inbound.feats.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.feats.findall.mapper.FeatViewModelMapper;
import com.dndplatform.compendium.domain.FeatFindByIdService;
import com.dndplatform.compendium.view.model.FeatFindByIdResource;
import com.dndplatform.compendium.view.model.vm.FeatViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class FeatFindByIdDelegate implements FeatFindByIdResource {

    private final FeatFindByIdService service;
    private final FeatViewModelMapper mapper;

    @Inject
    public FeatFindByIdDelegate(FeatFindByIdService service,
                                FeatViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public FeatViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

package com.dndplatform.compendium.adapter.inbound.magicitems.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.magicitems.findall.mapper.MagicItemViewModelMapper;
import com.dndplatform.compendium.domain.MagicItemFindByIdService;
import com.dndplatform.compendium.view.model.MagicItemFindByIdResource;
import com.dndplatform.compendium.view.model.vm.MagicItemViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class MagicItemFindByIdDelegate implements MagicItemFindByIdResource {

    private final MagicItemFindByIdService service;
    private final MagicItemViewModelMapper mapper;

    @Inject
    public MagicItemFindByIdDelegate(MagicItemFindByIdService service,
                                     MagicItemViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public MagicItemViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

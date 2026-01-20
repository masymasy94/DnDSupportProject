package com.dndplatform.compendium.adapter.inbound.weapontypes.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.weapontypes.findall.mapper.WeaponTypeViewModelMapper;
import com.dndplatform.compendium.domain.WeaponTypeFindByIdService;
import com.dndplatform.compendium.view.model.WeaponTypeFindByIdResource;
import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class WeaponTypeFindByIdDelegate implements WeaponTypeFindByIdResource {

    private final WeaponTypeFindByIdService service;
    private final WeaponTypeViewModelMapper mapper;

    @Inject
    public WeaponTypeFindByIdDelegate(WeaponTypeFindByIdService service,
                                      WeaponTypeViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public WeaponTypeViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

package com.dndplatform.compendium.adapter.inbound.weapontypes.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.weapontypes.findall.mapper.WeaponTypeViewModelMapper;
import com.dndplatform.compendium.domain.WeaponTypeFindAllService;
import com.dndplatform.compendium.view.model.WeaponTypeFindAllResource;
import com.dndplatform.compendium.view.model.vm.WeaponTypeViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;

@Delegate
@RequestScoped
public class WeaponTypeFindAllDelegate implements WeaponTypeFindAllResource {

    private final WeaponTypeFindAllService service;
    private final WeaponTypeViewModelMapper mapper;

    @Inject
    public WeaponTypeFindAllDelegate(WeaponTypeFindAllService service,
                                     WeaponTypeViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<WeaponTypeViewModel> findAll(String category) {
        return service.findAll(category).stream()
                .map(mapper)
                .toList();
    }
}

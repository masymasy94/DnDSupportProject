package com.dndplatform.compendium.adapter.inbound.equipment.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.equipment.findall.mapper.EquipmentViewModelMapper;
import com.dndplatform.compendium.domain.EquipmentFindByIdService;
import com.dndplatform.compendium.view.model.EquipmentFindByIdResource;
import com.dndplatform.compendium.view.model.vm.EquipmentViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class EquipmentFindByIdDelegate implements EquipmentFindByIdResource {

    private final EquipmentFindByIdService service;
    private final EquipmentViewModelMapper mapper;

    @Inject
    public EquipmentFindByIdDelegate(EquipmentFindByIdService service,
                                     EquipmentViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public EquipmentViewModel findById(int id) {
        var model = service.findById(id);
        return mapper.apply(model);
    }
}

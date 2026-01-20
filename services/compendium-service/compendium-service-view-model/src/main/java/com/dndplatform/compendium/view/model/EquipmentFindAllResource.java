package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.PagedEquipmentViewModel;

public interface EquipmentFindAllResource {
    PagedEquipmentViewModel findAll(
            String name,
            String category,
            Integer page,
            Integer pageSize
    );
}

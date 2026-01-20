package com.dndplatform.compendium.view.model;

import com.dndplatform.compendium.view.model.vm.EquipmentViewModel;

public interface EquipmentFindByIdResource {
    EquipmentViewModel findById(int id);
}

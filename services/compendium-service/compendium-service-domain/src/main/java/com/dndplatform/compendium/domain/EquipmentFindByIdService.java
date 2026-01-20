package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.Equipment;

public interface EquipmentFindByIdService {
    Equipment findById(int id);
}

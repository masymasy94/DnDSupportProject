package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.PagedResult;

public interface EquipmentFindAllRepository {
    PagedResult<Equipment> findAllEquipment(EquipmentFilterCriteria criteria);
}

package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.PagedResult;

public interface EquipmentFindAllService {
    PagedResult<Equipment> findAll(EquipmentFilterCriteria criteria);
}

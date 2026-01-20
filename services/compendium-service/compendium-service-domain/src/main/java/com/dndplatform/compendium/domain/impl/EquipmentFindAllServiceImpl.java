package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.EquipmentFindAllService;
import com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.EquipmentFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EquipmentFindAllServiceImpl implements EquipmentFindAllService {

    private final EquipmentFindAllRepository repository;

    @Inject
    public EquipmentFindAllServiceImpl(EquipmentFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public PagedResult<Equipment> findAll(EquipmentFilterCriteria criteria) {
        return repository.findAllEquipment(criteria);
    }
}

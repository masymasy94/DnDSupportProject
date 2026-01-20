package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.EquipmentFindByIdService;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.repository.EquipmentFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EquipmentFindByIdServiceImpl implements EquipmentFindByIdService {

    private final EquipmentFindByIdRepository repository;

    @Inject
    public EquipmentFindByIdServiceImpl(EquipmentFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Equipment findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Equipment not found with id: " + id));
    }
}

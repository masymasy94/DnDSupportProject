package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Equipment;

import java.util.Optional;

public interface EquipmentFindByIdRepository {
    Optional<Equipment> findById(int id);
}

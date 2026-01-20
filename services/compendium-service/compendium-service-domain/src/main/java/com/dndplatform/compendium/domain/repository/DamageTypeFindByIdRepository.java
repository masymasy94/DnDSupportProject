package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.DamageType;

import java.util.Optional;

public interface DamageTypeFindByIdRepository {
    Optional<DamageType> findById(int id);
}

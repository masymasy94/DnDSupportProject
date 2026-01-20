package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.WeaponType;

import java.util.Optional;

public interface WeaponTypeFindByIdRepository {
    Optional<WeaponType> findById(int id);
}

package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.ArmorType;

import java.util.Optional;

public interface ArmorTypeFindByIdRepository {
    Optional<ArmorType> findById(int id);
}

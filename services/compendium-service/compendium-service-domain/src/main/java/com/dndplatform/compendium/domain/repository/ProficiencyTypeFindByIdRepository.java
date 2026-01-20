package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.ProficiencyType;

import java.util.Optional;

public interface ProficiencyTypeFindByIdRepository {
    Optional<ProficiencyType> findById(int id);
}

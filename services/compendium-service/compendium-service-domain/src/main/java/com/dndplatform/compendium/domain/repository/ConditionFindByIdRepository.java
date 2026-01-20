package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Condition;

import java.util.Optional;

public interface ConditionFindByIdRepository {
    Optional<Condition> findById(int id);
}

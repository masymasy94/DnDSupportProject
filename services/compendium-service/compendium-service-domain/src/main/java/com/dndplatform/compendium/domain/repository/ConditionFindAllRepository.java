package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Condition;

import java.util.List;

public interface ConditionFindAllRepository {
    List<Condition> findAllConditions();
}

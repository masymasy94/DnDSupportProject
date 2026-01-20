package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.DamageType;

import java.util.List;

public interface DamageTypeFindAllRepository {
    List<DamageType> findAllDamageTypes();
}

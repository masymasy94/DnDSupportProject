package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.SpellSchool;

import java.util.Optional;

public interface SpellSchoolFindByIdRepository {
    Optional<SpellSchool> findById(int id);
}

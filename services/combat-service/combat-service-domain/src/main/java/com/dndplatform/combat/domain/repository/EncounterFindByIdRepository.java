package com.dndplatform.combat.domain.repository;

import com.dndplatform.combat.domain.model.Encounter;

import java.util.Optional;

public interface EncounterFindByIdRepository {
    Optional<Encounter> findById(Long id);
}

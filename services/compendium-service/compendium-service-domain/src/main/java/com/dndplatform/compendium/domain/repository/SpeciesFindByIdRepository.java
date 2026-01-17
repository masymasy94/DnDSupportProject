package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Species;

import java.util.Optional;

public interface SpeciesFindByIdRepository {
    Optional<Species> findById(int id);
}

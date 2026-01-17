package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.Species;

import java.util.Optional;

public interface SpeciesFindByIdService {
    Optional<Species> findById(int id);
}

package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Feat;

import java.util.Optional;

public interface FeatFindByIdRepository {
    Optional<Feat> findById(int id);
}

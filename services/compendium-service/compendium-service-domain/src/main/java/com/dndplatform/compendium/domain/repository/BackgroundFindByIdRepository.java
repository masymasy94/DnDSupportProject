package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Background;

import java.util.Optional;

public interface BackgroundFindByIdRepository {
    Optional<Background> findById(int id);
}

package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Monster;

import java.util.Optional;

public interface MonsterFindByIdRepository {
    Optional<Monster> findById(int id);
}

package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.CharacterClass;

import java.util.Optional;

public interface CharacterClassFindByIdRepository {
    Optional<CharacterClass> findById(int id);
}

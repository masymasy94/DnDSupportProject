package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Alignment;

import java.util.Optional;

public interface AlignmentFindByIdRepository {
    Optional<Alignment> findById(int id);
}

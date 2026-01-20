package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.ToolType;

import java.util.Optional;

public interface ToolTypeFindByIdRepository {
    Optional<ToolType> findById(int id);
}

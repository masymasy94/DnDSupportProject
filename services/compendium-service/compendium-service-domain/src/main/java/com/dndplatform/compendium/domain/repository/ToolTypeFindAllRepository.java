package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.ToolType;

import java.util.List;

public interface ToolTypeFindAllRepository {
    List<ToolType> findAllToolTypes(String category);
}

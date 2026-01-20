package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.ToolType;

import java.util.List;

public interface ToolTypeFindAllService {
    List<ToolType> findAll(String category);
}

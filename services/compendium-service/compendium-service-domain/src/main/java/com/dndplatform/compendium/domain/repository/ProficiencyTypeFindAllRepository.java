package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.ProficiencyType;

import java.util.List;

public interface ProficiencyTypeFindAllRepository {
    List<ProficiencyType> findAllProficiencyTypes();
}

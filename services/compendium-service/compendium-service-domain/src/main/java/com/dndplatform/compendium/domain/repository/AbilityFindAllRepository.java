package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Ability;

import java.util.List;

public interface AbilityFindAllRepository {
    List<Ability> findAll();
}

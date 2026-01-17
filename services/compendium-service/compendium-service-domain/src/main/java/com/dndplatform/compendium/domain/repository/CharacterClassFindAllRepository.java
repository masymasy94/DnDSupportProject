package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.CharacterClass;

import java.util.List;

public interface CharacterClassFindAllRepository {
    List<CharacterClass> findAllClasses();
}

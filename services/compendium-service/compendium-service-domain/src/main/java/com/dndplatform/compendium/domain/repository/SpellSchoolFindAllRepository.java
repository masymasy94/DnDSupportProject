package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.SpellSchool;

import java.util.List;

public interface SpellSchoolFindAllRepository {
    List<SpellSchool> findAllSpellSchools();
}

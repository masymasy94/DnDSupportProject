package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.Spell;

import java.util.List;

public interface SpellFindAllRepository {
    List<Spell> findAllSpells(SpellFilterCriteria criteria);
}

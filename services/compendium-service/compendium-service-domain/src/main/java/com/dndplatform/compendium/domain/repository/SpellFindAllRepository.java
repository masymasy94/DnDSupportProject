package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.model.Spell;

public interface SpellFindAllRepository {
    PagedResult<Spell> findAllSpells(SpellFilterCriteria criteria);
}

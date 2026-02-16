package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.model.Spell;

public interface SpellFindAllService {
    PagedResult<Spell> findAll(SpellFilterCriteria criteria);
}

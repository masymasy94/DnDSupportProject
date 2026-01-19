package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.Spell;

import java.util.List;

public interface SpellFindAllService {
    List<Spell> findAll(SpellFilterCriteria criteria);
}

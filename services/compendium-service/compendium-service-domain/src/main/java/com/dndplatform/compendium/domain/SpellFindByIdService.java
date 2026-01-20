package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.Spell;

public interface SpellFindByIdService {
    Spell findById(int id);
}

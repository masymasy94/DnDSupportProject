package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Spell;

import java.util.Optional;

public interface SpellFindByIdRepository {
    Optional<Spell> findById(int id);
}

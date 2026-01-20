package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.SpellFindByIdService;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.domain.repository.SpellFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SpellFindByIdServiceImpl implements SpellFindByIdService {

    private final SpellFindByIdRepository repository;

    @Inject
    public SpellFindByIdServiceImpl(SpellFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Spell findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Spell not found with id: " + id));
    }
}

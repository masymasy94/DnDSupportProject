package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.SpellFindAllService;
import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.domain.repository.SpellFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SpellFindAllServiceImpl implements SpellFindAllService {

    private final SpellFindAllRepository repository;

    @Inject
    public SpellFindAllServiceImpl(SpellFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Spell> findAll(SpellFilterCriteria criteria) {
        return repository.findAllSpells(criteria);
    }
}

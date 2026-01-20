package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.SpellSchoolFindByIdService;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.domain.repository.SpellSchoolFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SpellSchoolFindByIdServiceImpl implements SpellSchoolFindByIdService {

    private final SpellSchoolFindByIdRepository repository;

    @Inject
    public SpellSchoolFindByIdServiceImpl(SpellSchoolFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public SpellSchool findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Spell school not found with id: " + id));
    }
}

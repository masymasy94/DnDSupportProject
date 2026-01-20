package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.SpellSchoolFindAllService;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.domain.repository.SpellSchoolFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SpellSchoolFindAllServiceImpl implements SpellSchoolFindAllService {

    private final SpellSchoolFindAllRepository repository;

    @Inject
    public SpellSchoolFindAllServiceImpl(SpellSchoolFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SpellSchool> findAll() {
        return repository.findAllSpellSchools();
    }
}

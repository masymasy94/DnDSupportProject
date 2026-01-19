package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.SpeciesFindAllService;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.domain.repository.SpeciesFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SpeciesFindAllServiceImpl implements SpeciesFindAllService {

    private final SpeciesFindAllRepository repository;

    @Inject
    public SpeciesFindAllServiceImpl(SpeciesFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Species> findAll() {
        return repository.findAllSpecies();
    }
}

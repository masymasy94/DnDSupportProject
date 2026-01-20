package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.DamageTypeFindAllService;
import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.domain.repository.DamageTypeFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class DamageTypeFindAllServiceImpl implements DamageTypeFindAllService {

    private final DamageTypeFindAllRepository repository;

    @Inject
    public DamageTypeFindAllServiceImpl(DamageTypeFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DamageType> findAll() {
        return repository.findAllDamageTypes();
    }
}

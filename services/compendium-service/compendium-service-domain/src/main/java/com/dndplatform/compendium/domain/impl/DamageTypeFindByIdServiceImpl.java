package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.DamageTypeFindByIdService;
import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.domain.repository.DamageTypeFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DamageTypeFindByIdServiceImpl implements DamageTypeFindByIdService {

    private final DamageTypeFindByIdRepository repository;

    @Inject
    public DamageTypeFindByIdServiceImpl(DamageTypeFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public DamageType findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Damage type not found with id: " + id));
    }
}

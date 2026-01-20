package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.ArmorTypeFindByIdService;
import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ArmorTypeFindByIdServiceImpl implements ArmorTypeFindByIdService {

    private final ArmorTypeFindByIdRepository repository;

    @Inject
    public ArmorTypeFindByIdServiceImpl(ArmorTypeFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public ArmorType findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Armor type not found with id: " + id));
    }
}

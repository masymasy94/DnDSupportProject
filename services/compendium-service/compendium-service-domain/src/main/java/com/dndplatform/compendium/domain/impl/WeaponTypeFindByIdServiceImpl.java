package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.WeaponTypeFindByIdService;
import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.domain.repository.WeaponTypeFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class WeaponTypeFindByIdServiceImpl implements WeaponTypeFindByIdService {

    private final WeaponTypeFindByIdRepository repository;

    @Inject
    public WeaponTypeFindByIdServiceImpl(WeaponTypeFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public WeaponType findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Weapon type not found with id: " + id));
    }
}

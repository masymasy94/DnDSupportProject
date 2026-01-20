package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.WeaponTypeFindAllService;
import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.domain.repository.WeaponTypeFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class WeaponTypeFindAllServiceImpl implements WeaponTypeFindAllService {

    private final WeaponTypeFindAllRepository repository;

    @Inject
    public WeaponTypeFindAllServiceImpl(WeaponTypeFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<WeaponType> findAll(String category) {
        return repository.findAllWeaponTypes(category);
    }
}

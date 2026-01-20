package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.ArmorTypeFindAllService;
import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ArmorTypeFindAllServiceImpl implements ArmorTypeFindAllService {

    private final ArmorTypeFindAllRepository repository;

    @Inject
    public ArmorTypeFindAllServiceImpl(ArmorTypeFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ArmorType> findAll() {
        return repository.findAllArmorTypes();
    }
}

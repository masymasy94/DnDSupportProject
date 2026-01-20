package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.BackgroundFindAllService;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.domain.repository.BackgroundFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class BackgroundFindAllServiceImpl implements BackgroundFindAllService {

    private final BackgroundFindAllRepository repository;

    @Inject
    public BackgroundFindAllServiceImpl(BackgroundFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Background> findAll() {
        return repository.findAllBackgrounds();
    }
}

package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.BackgroundFindByIdService;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.domain.repository.BackgroundFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class BackgroundFindByIdServiceImpl implements BackgroundFindByIdService {

    private final BackgroundFindByIdRepository repository;

    @Inject
    public BackgroundFindByIdServiceImpl(BackgroundFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Background findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Background not found with id: " + id));
    }
}

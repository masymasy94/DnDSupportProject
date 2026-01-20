package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.FeatFindByIdService;
import com.dndplatform.compendium.domain.model.Feat;
import com.dndplatform.compendium.domain.repository.FeatFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class FeatFindByIdServiceImpl implements FeatFindByIdService {

    private final FeatFindByIdRepository repository;

    @Inject
    public FeatFindByIdServiceImpl(FeatFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Feat findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Feat not found with id: " + id));
    }
}

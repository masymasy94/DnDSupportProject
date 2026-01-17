package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.domain.repository.SpeciesFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class SpeciesFindByIdServiceImpl implements SpeciesFindByIdService {

    private final SpeciesFindByIdRepository repository;

    @Inject
    public SpeciesFindByIdServiceImpl(SpeciesFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Species> findById(int id) {
        return repository.findById(id);
    }
}

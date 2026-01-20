package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.AlignmentFindByIdService;
import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.domain.repository.AlignmentFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AlignmentFindByIdServiceImpl implements AlignmentFindByIdService {

    private final AlignmentFindByIdRepository repository;

    @Inject
    public AlignmentFindByIdServiceImpl(AlignmentFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Alignment findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Alignment not found with id: " + id));
    }
}

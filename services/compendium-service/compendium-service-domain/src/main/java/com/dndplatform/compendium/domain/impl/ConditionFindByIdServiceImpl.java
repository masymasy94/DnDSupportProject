package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.ConditionFindByIdService;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.domain.repository.ConditionFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ConditionFindByIdServiceImpl implements ConditionFindByIdService {

    private final ConditionFindByIdRepository repository;

    @Inject
    public ConditionFindByIdServiceImpl(ConditionFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Condition findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Condition not found with id: " + id));
    }
}

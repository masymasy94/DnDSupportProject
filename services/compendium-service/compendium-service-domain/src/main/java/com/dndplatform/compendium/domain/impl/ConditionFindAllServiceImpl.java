package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.ConditionFindAllService;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.domain.repository.ConditionFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ConditionFindAllServiceImpl implements ConditionFindAllService {

    private final ConditionFindAllRepository repository;

    @Inject
    public ConditionFindAllServiceImpl(ConditionFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Condition> findAll() {
        return repository.findAllConditions();
    }
}

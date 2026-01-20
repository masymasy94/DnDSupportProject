package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.MonsterFindByIdService;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.repository.MonsterFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MonsterFindByIdServiceImpl implements MonsterFindByIdService {

    private final MonsterFindByIdRepository repository;

    @Inject
    public MonsterFindByIdServiceImpl(MonsterFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Monster findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Monster not found with id: " + id));
    }
}

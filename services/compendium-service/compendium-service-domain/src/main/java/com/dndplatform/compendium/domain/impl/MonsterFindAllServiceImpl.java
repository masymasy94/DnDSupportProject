package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.MonsterFindAllService;
import com.dndplatform.compendium.domain.filter.MonsterFilterCriteria;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.MonsterFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MonsterFindAllServiceImpl implements MonsterFindAllService {

    private final MonsterFindAllRepository repository;

    @Inject
    public MonsterFindAllServiceImpl(MonsterFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public PagedResult<Monster> findAll(MonsterFilterCriteria criteria) {
        return repository.findAllMonsters(criteria);
    }
}

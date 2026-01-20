package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.FeatFindAllService;
import com.dndplatform.compendium.domain.model.Feat;
import com.dndplatform.compendium.domain.repository.FeatFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class FeatFindAllServiceImpl implements FeatFindAllService {

    private final FeatFindAllRepository repository;

    @Inject
    public FeatFindAllServiceImpl(FeatFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Feat> findAll() {
        return repository.findAllFeats();
    }
}

package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.AlignmentFindAllService;
import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.domain.repository.AlignmentFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class AlignmentFindAllServiceImpl implements AlignmentFindAllService {

    private final AlignmentFindAllRepository repository;

    @Inject
    public AlignmentFindAllServiceImpl(AlignmentFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Alignment> findAll() {
        return repository.findAllAlignment();
    }
}

package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.ProficiencyTypeFindAllService;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProficiencyTypeFindAllServiceImpl implements ProficiencyTypeFindAllService {

    private final ProficiencyTypeFindAllRepository repository;

    @Inject
    public ProficiencyTypeFindAllServiceImpl(ProficiencyTypeFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProficiencyType> findAll() {
        return repository.findAllProficiencyTypes();
    }
}

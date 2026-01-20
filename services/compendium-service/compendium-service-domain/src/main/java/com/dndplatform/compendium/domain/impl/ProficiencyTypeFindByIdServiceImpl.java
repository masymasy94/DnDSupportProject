package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.ProficiencyTypeFindByIdService;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProficiencyTypeFindByIdServiceImpl implements ProficiencyTypeFindByIdService {

    private final ProficiencyTypeFindByIdRepository repository;

    @Inject
    public ProficiencyTypeFindByIdServiceImpl(ProficiencyTypeFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProficiencyType findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proficiency type not found with id: " + id));
    }
}

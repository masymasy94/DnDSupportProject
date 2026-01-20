package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.SkillFindByIdService;
import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.domain.repository.SkillFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SkillFindByIdServiceImpl implements SkillFindByIdService {

    private final SkillFindByIdRepository repository;

    @Inject
    public SkillFindByIdServiceImpl(SkillFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Skill findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Skill not found with id: " + id));
    }
}

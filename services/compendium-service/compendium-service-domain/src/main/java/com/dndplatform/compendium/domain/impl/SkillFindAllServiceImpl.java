package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.SkillFindAllService;
import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.domain.repository.SkillFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SkillFindAllServiceImpl implements SkillFindAllService {

    private final SkillFindAllRepository repository;

    @Inject
    public SkillFindAllServiceImpl(SkillFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Skill> findAll() {
        return repository.findAllSkills();
    }
}

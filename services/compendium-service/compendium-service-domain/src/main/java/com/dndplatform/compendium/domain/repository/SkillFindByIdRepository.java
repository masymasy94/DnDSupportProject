package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Skill;

import java.util.Optional;

public interface SkillFindByIdRepository {
    Optional<Skill> findById(int id);
}

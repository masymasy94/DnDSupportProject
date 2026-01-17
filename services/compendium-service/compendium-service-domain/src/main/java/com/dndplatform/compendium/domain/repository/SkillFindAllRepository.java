package com.dndplatform.compendium.domain.repository;

import com.dndplatform.compendium.domain.model.Skill;

import java.util.List;

public interface SkillFindAllRepository {
    List<Skill> findAll();
}

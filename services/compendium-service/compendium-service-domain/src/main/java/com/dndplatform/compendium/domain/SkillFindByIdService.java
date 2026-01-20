package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.Skill;

public interface SkillFindByIdService {
    Skill findById(int id);
}

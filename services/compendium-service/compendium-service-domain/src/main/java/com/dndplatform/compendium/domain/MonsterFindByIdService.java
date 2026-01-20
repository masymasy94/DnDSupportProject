package com.dndplatform.compendium.domain;

import com.dndplatform.compendium.domain.model.Monster;

public interface MonsterFindByIdService {
    Monster findById(int id);
}

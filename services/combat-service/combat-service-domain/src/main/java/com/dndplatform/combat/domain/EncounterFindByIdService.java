package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;

public interface EncounterFindByIdService {
    Encounter findById(Long id);
}

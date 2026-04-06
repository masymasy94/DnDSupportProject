package com.dndplatform.combat.domain.repository;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterCreate;

public interface EncounterCreateRepository {
    Encounter save(EncounterCreate input);
}

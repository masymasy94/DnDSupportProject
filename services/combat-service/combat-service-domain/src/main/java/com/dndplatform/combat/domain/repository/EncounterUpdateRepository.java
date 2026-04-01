package com.dndplatform.combat.domain.repository;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterUpdate;

public interface EncounterUpdateRepository {
    Encounter update(EncounterUpdate input);
}

package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterUpdate;

public interface EncounterUpdateService {
    Encounter update(Long userId, EncounterUpdate input);
}

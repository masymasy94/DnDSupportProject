package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterCreate;

public interface EncounterCreateService {
    Encounter create(EncounterCreate input);
}

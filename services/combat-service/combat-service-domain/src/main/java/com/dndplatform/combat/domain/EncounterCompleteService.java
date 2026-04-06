package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;

public interface EncounterCompleteService {
    Encounter complete(Long encounterId, Long userId);
}

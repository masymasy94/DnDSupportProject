package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;

public interface InitiativeStartService {
    Encounter start(Long encounterId, Long userId);
}

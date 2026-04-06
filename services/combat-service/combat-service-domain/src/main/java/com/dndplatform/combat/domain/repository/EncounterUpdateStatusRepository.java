package com.dndplatform.combat.domain.repository;

import com.dndplatform.combat.domain.model.EncounterStatus;

public interface EncounterUpdateStatusRepository {
    void updateStatus(Long id, EncounterStatus status);
}

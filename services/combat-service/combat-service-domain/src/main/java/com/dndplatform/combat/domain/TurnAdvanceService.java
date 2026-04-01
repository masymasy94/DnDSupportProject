package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.EncounterParticipant;

import java.util.List;

public interface TurnAdvanceService {
    List<EncounterParticipant> advance(Long encounterId, Long userId);
}

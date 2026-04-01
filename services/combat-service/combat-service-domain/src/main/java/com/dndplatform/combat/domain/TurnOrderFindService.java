package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.EncounterParticipant;

import java.util.List;

public interface TurnOrderFindService {
    List<EncounterParticipant> getTurnOrder(Long encounterId);
}

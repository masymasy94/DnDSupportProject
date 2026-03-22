package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantCreate;

public interface ParticipantAddService {
    EncounterParticipant add(Long encounterId, Long userId, ParticipantCreate input);
}

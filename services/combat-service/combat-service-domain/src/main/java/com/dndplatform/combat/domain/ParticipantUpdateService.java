package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantUpdate;

public interface ParticipantUpdateService {
    EncounterParticipant update(Long encounterId, Long userId, ParticipantUpdate input);
}

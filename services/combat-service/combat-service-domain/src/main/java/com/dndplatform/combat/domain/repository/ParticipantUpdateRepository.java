package com.dndplatform.combat.domain.repository;

import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantUpdate;

public interface ParticipantUpdateRepository {
    EncounterParticipant update(ParticipantUpdate input);
}

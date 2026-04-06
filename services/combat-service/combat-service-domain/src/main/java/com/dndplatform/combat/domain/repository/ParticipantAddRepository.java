package com.dndplatform.combat.domain.repository;

import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantCreate;

public interface ParticipantAddRepository {
    EncounterParticipant add(Long encounterId, ParticipantCreate input);
}

package com.dndplatform.combat.domain.repository;

import com.dndplatform.combat.domain.model.EncounterParticipant;

import java.util.List;

public interface ParticipantFindByEncounterRepository {
    List<EncounterParticipant> findByEncounterId(Long encounterId);
}

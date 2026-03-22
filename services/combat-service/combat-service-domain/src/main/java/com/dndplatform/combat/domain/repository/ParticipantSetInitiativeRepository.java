package com.dndplatform.combat.domain.repository;

import java.util.Map;

public interface ParticipantSetInitiativeRepository {
    void setInitiatives(Long encounterId, Map<Long, Integer> initiativeMap);
}

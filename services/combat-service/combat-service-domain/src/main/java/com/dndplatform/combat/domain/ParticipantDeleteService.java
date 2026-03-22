package com.dndplatform.combat.domain;

public interface ParticipantDeleteService {
    void delete(Long encounterId, Long participantId, Long userId);
}

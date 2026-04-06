package com.dndplatform.combat.view.model;

public interface ParticipantDeleteResource {
    void delete(Long encounterId, Long participantId, Long userId);
}

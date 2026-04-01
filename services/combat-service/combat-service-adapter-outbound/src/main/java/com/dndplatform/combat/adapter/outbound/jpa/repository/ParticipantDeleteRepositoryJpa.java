package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterParticipantEntity;
import com.dndplatform.combat.domain.repository.ParticipantDeleteRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantDeleteRepositoryJpa implements ParticipantDeleteRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void deleteById(Long participantId) {
        log.info(() -> "Deleting participant: %d".formatted(participantId));

        EncounterParticipantEntity entity = EncounterParticipantEntity.findById(participantId);
        if (entity == null) {
            throw new NotFoundException("Participant not found with ID: %d".formatted(participantId));
        }

        entity.encounter.participants.remove(entity);
        entity.delete();

        log.info(() -> "Participant %d deleted successfully".formatted(participantId));
    }
}

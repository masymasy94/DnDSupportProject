package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.domain.repository.ParticipantDeleteRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantDeleteRepositoryJpa implements ParticipantDeleteRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ParticipantPanacheRepository participantRepository;

    @Inject
    public ParticipantDeleteRepositoryJpa(ParticipantPanacheRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    @Override
    @Transactional
    public void deleteById(Long participantId) {
        log.info(() -> "Deleting participant: %d".formatted(participantId));

        var entity = participantRepository.findByIdOptional(participantId)
                .orElseThrow(() -> new NotFoundException("Participant not found with ID: %d".formatted(participantId)));

        entity.encounter.participants.remove(entity);
        participantRepository.delete(entity);

        log.info(() -> "Participant %d deleted successfully".formatted(participantId));
    }
}

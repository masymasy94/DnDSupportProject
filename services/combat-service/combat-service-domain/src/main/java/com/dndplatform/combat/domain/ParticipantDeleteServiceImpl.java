package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.ParticipantDeleteRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantDeleteServiceImpl implements ParticipantDeleteService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterFindByIdRepository findRepository;
    private final ParticipantDeleteRepository deleteRepository;

    @Inject
    public ParticipantDeleteServiceImpl(EncounterFindByIdRepository findRepository,
                                        ParticipantDeleteRepository deleteRepository) {
        this.findRepository = findRepository;
        this.deleteRepository = deleteRepository;
    }

    @Override
    public void delete(Long encounterId, Long participantId, Long userId) {
        log.info(() -> "Deleting participant %d from encounter %d by user %d".formatted(participantId, encounterId, userId));

        Encounter encounter = findRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        if (!encounter.createdByUserId().equals(userId)) {
            throw new ForbiddenException("Only the Dungeon Master can remove participants from this encounter");
        }

        deleteRepository.deleteById(participantId);
        log.info(() -> "Participant %d deleted from encounter %d".formatted(participantId, encounterId));
    }
}

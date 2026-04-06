package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantUpdate;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.ParticipantUpdateRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantUpdateServiceImpl implements ParticipantUpdateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterFindByIdRepository findRepository;
    private final ParticipantUpdateRepository updateRepository;

    @Inject
    public ParticipantUpdateServiceImpl(EncounterFindByIdRepository findRepository,
                                        ParticipantUpdateRepository updateRepository) {
        this.findRepository = findRepository;
        this.updateRepository = updateRepository;
    }

    @Override
    public EncounterParticipant update(Long encounterId, Long userId, ParticipantUpdate input) {
        log.info(() -> "Updating participant %d in encounter %d by user %d".formatted(input.id(), encounterId, userId));

        Encounter encounter = findRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        if (!encounter.createdByUserId().equals(userId)) {
            throw new ForbiddenException("Only the Dungeon Master can update participants in this encounter");
        }

        EncounterParticipant updated = updateRepository.update(input);
        log.info(() -> "Participant %d updated in encounter %d".formatted(input.id(), encounterId));
        return updated;
    }
}

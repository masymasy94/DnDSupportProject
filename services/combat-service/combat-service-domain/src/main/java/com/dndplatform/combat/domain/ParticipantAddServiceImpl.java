package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.ParticipantCreate;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.ParticipantAddRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class ParticipantAddServiceImpl implements ParticipantAddService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterFindByIdRepository findRepository;
    private final ParticipantAddRepository addRepository;

    @Inject
    public ParticipantAddServiceImpl(EncounterFindByIdRepository findRepository,
                                     ParticipantAddRepository addRepository) {
        this.findRepository = findRepository;
        this.addRepository = addRepository;
    }

    @Override
    public EncounterParticipant add(Long encounterId, Long userId, ParticipantCreate input) {
        log.info(() -> "Adding participant '%s' to encounter %d by user %d".formatted(input.name(), encounterId, userId));

        Encounter encounter = findRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        if (!encounter.createdByUserId().equals(userId)) {
            throw new ForbiddenException("Only the Dungeon Master can add participants to this encounter");
        }

        EncounterParticipant participant = addRepository.add(encounterId, input);
        log.info(() -> "Participant '%s' added to encounter %d".formatted(input.name(), encounterId));
        return participant;
    }
}

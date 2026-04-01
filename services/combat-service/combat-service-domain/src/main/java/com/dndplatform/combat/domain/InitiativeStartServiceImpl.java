package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterParticipant;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.EncounterUpdateStatusRepository;
import com.dndplatform.combat.domain.repository.ParticipantSetInitiativeRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

@ApplicationScoped
public class InitiativeStartServiceImpl implements InitiativeStartService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterFindByIdRepository findRepository;
    private final ParticipantSetInitiativeRepository setInitiativeRepository;
    private final EncounterUpdateStatusRepository updateStatusRepository;

    @Inject
    public InitiativeStartServiceImpl(EncounterFindByIdRepository findRepository,
                                      ParticipantSetInitiativeRepository setInitiativeRepository,
                                      EncounterUpdateStatusRepository updateStatusRepository) {
        this.findRepository = findRepository;
        this.setInitiativeRepository = setInitiativeRepository;
        this.updateStatusRepository = updateStatusRepository;
    }

    @Override
    public Encounter start(Long encounterId, Long userId) {
        log.info(() -> "Starting initiative for encounter %d by user %d".formatted(encounterId, userId));

        Encounter encounter = findRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        if (!encounter.createdByUserId().equals(userId)) {
            throw new ForbiddenException("Only the Dungeon Master can start initiative for this encounter");
        }

        if (encounter.status() != EncounterStatus.DRAFT) {
            throw new IllegalStateException("Initiative can only be started for encounters in DRAFT status");
        }

        Map<Long, Integer> initiativeMap = new HashMap<>();
        for (EncounterParticipant participant : encounter.participants()) {
            int roll = ThreadLocalRandom.current().nextInt(1, 21);
            initiativeMap.put(participant.id(), roll);
        }

        setInitiativeRepository.setInitiatives(encounterId, initiativeMap);
        updateStatusRepository.updateStatus(encounterId, EncounterStatus.ACTIVE);

        Encounter updated = findRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        log.info(() -> "Initiative started for encounter %d".formatted(encounterId));
        return updated;
    }
}

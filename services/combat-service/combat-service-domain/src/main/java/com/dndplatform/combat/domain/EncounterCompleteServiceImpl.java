package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.EncounterUpdateStatusRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class EncounterCompleteServiceImpl implements EncounterCompleteService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterFindByIdRepository findRepository;
    private final EncounterUpdateStatusRepository updateStatusRepository;

    @Inject
    public EncounterCompleteServiceImpl(EncounterFindByIdRepository findRepository,
                                        EncounterUpdateStatusRepository updateStatusRepository) {
        this.findRepository = findRepository;
        this.updateStatusRepository = updateStatusRepository;
    }

    @Override
    public Encounter complete(Long encounterId, Long userId) {
        log.info(() -> "Completing encounter %d by user %d".formatted(encounterId, userId));

        Encounter encounter = findRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        if (!encounter.createdByUserId().equals(userId)) {
            throw new ForbiddenException("Only the Dungeon Master can complete this encounter");
        }

        if (encounter.status() != EncounterStatus.ACTIVE) {
            throw new IllegalStateException("Only encounters in ACTIVE status can be completed");
        }

        updateStatusRepository.updateStatus(encounterId, EncounterStatus.COMPLETED);

        Encounter completed = findRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        log.info(() -> "Encounter %d completed successfully".formatted(encounterId));
        return completed;
    }
}

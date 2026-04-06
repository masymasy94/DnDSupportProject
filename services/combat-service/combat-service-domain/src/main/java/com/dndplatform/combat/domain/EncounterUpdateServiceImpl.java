package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterUpdate;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.combat.domain.repository.EncounterUpdateRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class EncounterUpdateServiceImpl implements EncounterUpdateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterFindByIdRepository findRepository;
    private final EncounterUpdateRepository updateRepository;

    @Inject
    public EncounterUpdateServiceImpl(EncounterFindByIdRepository findRepository,
                                      EncounterUpdateRepository updateRepository) {
        this.findRepository = findRepository;
        this.updateRepository = updateRepository;
    }

    @Override
    public Encounter update(Long userId, EncounterUpdate input) {
        log.info(() -> "Updating encounter %d by user %d".formatted(input.id(), userId));

        Encounter existing = findRepository.findById(input.id())
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(input.id())));

        if (!existing.createdByUserId().equals(userId)) {
            throw new ForbiddenException("Only the Dungeon Master can update this encounter");
        }

        Encounter updated = updateRepository.update(input);
        log.info(() -> "Encounter %d updated successfully".formatted(input.id()));
        return updated;
    }
}

package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.repository.EncounterDeleteRepository;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class EncounterDeleteServiceImpl implements EncounterDeleteService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterFindByIdRepository findRepository;
    private final EncounterDeleteRepository deleteRepository;

    @Inject
    public EncounterDeleteServiceImpl(EncounterFindByIdRepository findRepository,
                                      EncounterDeleteRepository deleteRepository) {
        this.findRepository = findRepository;
        this.deleteRepository = deleteRepository;
    }

    @Override
    public void delete(Long encounterId, Long userId) {
        log.info(() -> "Deleting encounter %d by user %d".formatted(encounterId, userId));

        Encounter existing = findRepository.findById(encounterId)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(encounterId)));

        if (!existing.createdByUserId().equals(userId)) {
            throw new ForbiddenException("Only the Dungeon Master can delete this encounter");
        }

        deleteRepository.deleteById(encounterId);
        log.info(() -> "Encounter %d deleted successfully".formatted(encounterId));
    }
}

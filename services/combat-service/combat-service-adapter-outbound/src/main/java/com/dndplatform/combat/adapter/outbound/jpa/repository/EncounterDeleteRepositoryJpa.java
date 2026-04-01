package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.domain.repository.EncounterDeleteRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class EncounterDeleteRepositoryJpa implements EncounterDeleteRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info(() -> "Deleting encounter: %d".formatted(id));

        EncounterEntity entity = EncounterEntity.findById(id);
        if (entity == null) {
            throw new NotFoundException("Encounter not found with ID: %d".formatted(id));
        }

        entity.delete();
        log.info(() -> "Encounter %d deleted successfully".formatted(id));
    }
}

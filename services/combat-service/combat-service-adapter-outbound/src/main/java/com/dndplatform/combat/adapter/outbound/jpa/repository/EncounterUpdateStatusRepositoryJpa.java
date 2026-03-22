package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterUpdateStatusRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class EncounterUpdateStatusRepositoryJpa implements EncounterUpdateStatusRepository {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void updateStatus(Long id, EncounterStatus status) {
        log.info(() -> "Updating encounter %d status to %s".formatted(id, status));

        EncounterEntity entity = EncounterEntity.findById(id);
        if (entity == null) {
            throw new NotFoundException("Encounter not found with ID: %d".formatted(id));
        }

        entity.status = status.name();
        entity.updatedAt = LocalDateTime.now();
        entity.persist();

        log.info(() -> "Encounter %d status updated to %s".formatted(id, status));
    }
}

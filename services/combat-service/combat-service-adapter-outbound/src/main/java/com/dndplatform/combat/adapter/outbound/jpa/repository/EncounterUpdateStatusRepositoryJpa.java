package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.domain.model.EncounterStatus;
import com.dndplatform.combat.domain.repository.EncounterUpdateStatusRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class EncounterUpdateStatusRepositoryJpa implements EncounterUpdateStatusRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterPanacheRepository panacheRepository;

    @Inject
    public EncounterUpdateStatusRepositoryJpa(EncounterPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void updateStatus(Long id, EncounterStatus status) {
        log.info(() -> "Updating encounter %d status to %s".formatted(id, status));

        var entity = panacheRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(id)));

        entity.status = status.name();
        entity.updatedAt = LocalDateTime.now();
        panacheRepository.persist(entity);

        log.info(() -> "Encounter %d status updated to %s".formatted(id, status));
    }
}

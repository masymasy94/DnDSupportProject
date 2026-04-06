package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.domain.repository.EncounterDeleteRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

@ApplicationScoped
public class EncounterDeleteRepositoryJpa implements EncounterDeleteRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterPanacheRepository panacheRepository;

    @Inject
    public EncounterDeleteRepositoryJpa(EncounterPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info(() -> "Deleting encounter: %d".formatted(id));

        var entity = panacheRepository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(id)));

        panacheRepository.delete(entity);
        log.info(() -> "Encounter %d deleted successfully".formatted(id));
    }
}

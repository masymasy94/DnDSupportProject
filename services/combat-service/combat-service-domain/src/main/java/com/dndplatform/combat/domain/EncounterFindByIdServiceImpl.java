package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import com.dndplatform.common.exception.NotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class EncounterFindByIdServiceImpl implements EncounterFindByIdService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterFindByIdRepository repository;

    @Inject
    public EncounterFindByIdServiceImpl(EncounterFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Encounter findById(Long id) {
        log.info(() -> "Finding encounter by ID: %d".formatted(id));
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Encounter not found with ID: %d".formatted(id)));
    }
}

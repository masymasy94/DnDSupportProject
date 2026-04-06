package com.dndplatform.combat.domain;

import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.model.EncounterCreate;
import com.dndplatform.combat.domain.repository.EncounterCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class EncounterCreateServiceImpl implements EncounterCreateService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EncounterCreateRepository repository;

    @Inject
    public EncounterCreateServiceImpl(EncounterCreateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Encounter create(EncounterCreate input) {
        log.info(() -> "Creating encounter: %s".formatted(input.name()));

        Encounter encounter = repository.save(input);

        log.info(() -> "Encounter created with ID: %d".formatted(encounter.id()));
        return encounter;
    }
}

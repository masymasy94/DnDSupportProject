package com.dndplatform.combat.adapter.outbound.jpa.repository;

import com.dndplatform.combat.adapter.outbound.jpa.entity.EncounterEntity;
import com.dndplatform.combat.adapter.outbound.jpa.mapper.EncounterEntityMapper;
import com.dndplatform.combat.domain.model.Encounter;
import com.dndplatform.combat.domain.repository.EncounterFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class EncounterFindByIdRepositoryJpa implements EncounterFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EncounterEntityMapper mapper;

    @Inject
    public EncounterFindByIdRepositoryJpa(EncounterEntityMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Encounter> findById(Long id) {
        log.info(() -> "Finding encounter by ID: %d".formatted(id));

        EncounterEntity entity = EncounterEntity.findById(id);
        return Optional.ofNullable(entity).map(mapper::toEncounter);
    }
}

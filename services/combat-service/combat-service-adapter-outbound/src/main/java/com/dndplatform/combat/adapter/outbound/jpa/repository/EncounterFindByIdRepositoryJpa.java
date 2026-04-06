package com.dndplatform.combat.adapter.outbound.jpa.repository;

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
    private final EncounterPanacheRepository panacheRepository;

    @Inject
    public EncounterFindByIdRepositoryJpa(EncounterEntityMapper mapper,
                                          EncounterPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public Optional<Encounter> findById(Long id) {
        log.info(() -> "Finding encounter by ID: %d".formatted(id));

        return panacheRepository.findByIdOptional(id).map(mapper::toEncounter);
    }
}

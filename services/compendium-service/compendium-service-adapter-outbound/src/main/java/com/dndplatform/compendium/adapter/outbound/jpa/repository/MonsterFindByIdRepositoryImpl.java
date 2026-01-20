package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.MonsterEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.MonsterMapper;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.repository.MonsterFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class MonsterFindByIdRepositoryImpl implements MonsterFindByIdRepository, PanacheRepository<MonsterEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MonsterMapper mapper;

    @Inject
    public MonsterFindByIdRepositoryImpl(MonsterMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Monster> findById(int id) {
        log.info(() -> "Finding Monster with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

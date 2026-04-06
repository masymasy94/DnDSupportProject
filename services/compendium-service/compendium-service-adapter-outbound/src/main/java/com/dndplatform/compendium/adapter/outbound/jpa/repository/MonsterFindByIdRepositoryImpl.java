package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.MonsterMapper;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.repository.MonsterFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class MonsterFindByIdRepositoryImpl implements MonsterFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MonsterPanacheRepository panacheRepository;
    private final MonsterMapper mapper;

    @Inject
    public MonsterFindByIdRepositoryImpl(MonsterPanacheRepository panacheRepository, MonsterMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Monster> findById(int id) {
        log.info(() -> "Finding Monster with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

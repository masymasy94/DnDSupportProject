package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.BackgroundEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.BackgroundMapper;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.domain.repository.BackgroundFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class BackgroundFindByIdRepositoryImpl implements BackgroundFindByIdRepository, PanacheRepository<BackgroundEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final BackgroundMapper mapper;

    @Inject
    public BackgroundFindByIdRepositoryImpl(BackgroundMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Background> findById(int id) {
        log.info(() -> "Finding background with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

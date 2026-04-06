package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.BackgroundMapper;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.domain.repository.BackgroundFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class BackgroundFindByIdRepositoryImpl implements BackgroundFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final BackgroundPanacheRepository panacheRepository;
    private final BackgroundMapper mapper;

    @Inject
    public BackgroundFindByIdRepositoryImpl(BackgroundPanacheRepository panacheRepository, BackgroundMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Background> findById(int id) {
        log.info(() -> "Finding background with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

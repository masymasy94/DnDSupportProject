package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.BackgroundMapper;
import com.dndplatform.compendium.domain.model.Background;
import com.dndplatform.compendium.domain.repository.BackgroundFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class BackgroundFindAllRepositoryImpl implements BackgroundFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final BackgroundPanacheRepository panacheRepository;
    private final BackgroundMapper mapper;

    @Inject
    public BackgroundFindAllRepositoryImpl(BackgroundPanacheRepository panacheRepository, BackgroundMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Background> findAllBackgrounds() {
        log.info(() -> "Finding all backgrounds");

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}

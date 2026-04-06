package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpeciesMapper;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.domain.repository.SpeciesFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class SpeciesFindAllRepositoryImpl implements SpeciesFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SpeciesPanacheRepository panacheRepository;
    private final SpeciesMapper mapper;

    @Inject
    public SpeciesFindAllRepositoryImpl(SpeciesPanacheRepository panacheRepository, SpeciesMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Species> findAllSpecies() {
        log.info(() -> "Finding all species");

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}

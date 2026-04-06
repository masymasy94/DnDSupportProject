package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpeciesMapper;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.domain.repository.SpeciesFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class SpeciesFindByIdRepositoryImpl implements SpeciesFindByIdRepository {

    private final SpeciesPanacheRepository panacheRepository;
    private final SpeciesMapper mapper;

    @Inject
    public SpeciesFindByIdRepositoryImpl(SpeciesPanacheRepository panacheRepository, SpeciesMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Species> findById(int id) {
        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

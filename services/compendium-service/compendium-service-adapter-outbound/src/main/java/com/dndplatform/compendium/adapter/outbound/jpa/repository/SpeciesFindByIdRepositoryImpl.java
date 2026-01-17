package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpeciesEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpeciesMapper;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.domain.repository.SpeciesFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class SpeciesFindByIdRepositoryImpl implements SpeciesFindByIdRepository, PanacheRepository<SpeciesEntity> {

    private final SpeciesMapper mapper;

    @Inject
    public SpeciesFindByIdRepositoryImpl(SpeciesMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Species> findById(int id) {
        return findByIdOptional((long) id)
                .map(mapper);
    }
}

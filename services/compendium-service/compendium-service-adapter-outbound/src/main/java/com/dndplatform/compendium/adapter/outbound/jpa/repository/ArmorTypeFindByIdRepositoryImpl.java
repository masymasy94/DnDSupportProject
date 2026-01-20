package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ArmorTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ArmorTypeMapper;
import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ArmorTypeFindByIdRepositoryImpl implements ArmorTypeFindByIdRepository, PanacheRepository<ArmorTypeEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ArmorTypeMapper mapper;

    @Inject
    public ArmorTypeFindByIdRepositoryImpl(ArmorTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<ArmorType> findById(int id) {
        log.info(() -> "Finding armor type with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

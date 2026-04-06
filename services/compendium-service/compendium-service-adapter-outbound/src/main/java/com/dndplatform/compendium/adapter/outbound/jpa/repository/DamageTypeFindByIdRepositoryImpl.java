package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.DamageTypeMapper;
import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.domain.repository.DamageTypeFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class DamageTypeFindByIdRepositoryImpl implements DamageTypeFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final DamageTypePanacheRepository panacheRepository;
    private final DamageTypeMapper mapper;

    @Inject
    public DamageTypeFindByIdRepositoryImpl(DamageTypePanacheRepository panacheRepository, DamageTypeMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<DamageType> findById(int id) {
        log.info(() -> "Finding damage type with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

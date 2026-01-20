package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.DamageTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.DamageTypeMapper;
import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.domain.repository.DamageTypeFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DamageTypeFindAllRepositoryImpl implements DamageTypeFindAllRepository, PanacheRepository<DamageTypeEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final DamageTypeMapper mapper;

    @Inject
    public DamageTypeFindAllRepositoryImpl(DamageTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<DamageType> findAllDamageTypes() {
        log.info(() -> "Finding all damage types");

        return findAll(Sort.by("name")).list().stream()
                .map(mapper)
                .toList();
    }
}

package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.DamageTypeMapper;
import com.dndplatform.compendium.domain.model.DamageType;
import com.dndplatform.compendium.domain.repository.DamageTypeFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class DamageTypeFindAllRepositoryImpl implements DamageTypeFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final DamageTypePanacheRepository panacheRepository;
    private final DamageTypeMapper mapper;

    @Inject
    public DamageTypeFindAllRepositoryImpl(DamageTypePanacheRepository panacheRepository, DamageTypeMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<DamageType> findAllDamageTypes() {
        log.info(() -> "Finding all damage types");

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}

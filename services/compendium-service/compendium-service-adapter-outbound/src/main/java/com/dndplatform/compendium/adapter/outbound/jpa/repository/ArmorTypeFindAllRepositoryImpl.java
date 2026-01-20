package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ArmorTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ArmorTypeMapper;
import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ArmorTypeFindAllRepositoryImpl implements ArmorTypeFindAllRepository, PanacheRepository<ArmorTypeEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ArmorTypeMapper mapper;

    @Inject
    public ArmorTypeFindAllRepositoryImpl(ArmorTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<ArmorType> findAllArmorTypes() {
        log.info(() -> "Finding all armor types");

        return findAll(Sort.by("name")).list().stream()
                .map(mapper)
                .toList();
    }
}

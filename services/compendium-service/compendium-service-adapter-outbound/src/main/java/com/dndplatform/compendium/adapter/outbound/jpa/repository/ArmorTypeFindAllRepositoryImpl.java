package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ArmorTypeMapper;
import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ArmorTypeFindAllRepositoryImpl implements ArmorTypeFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ArmorTypePanacheRepository panacheRepository;
    private final ArmorTypeMapper mapper;

    @Inject
    public ArmorTypeFindAllRepositoryImpl(ArmorTypePanacheRepository panacheRepository, ArmorTypeMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ArmorType> findAllArmorTypes() {
        log.info(() -> "Finding all armor types");

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}

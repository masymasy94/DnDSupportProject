package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ArmorTypeMapper;
import com.dndplatform.compendium.domain.model.ArmorType;
import com.dndplatform.compendium.domain.repository.ArmorTypeFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ArmorTypeFindByIdRepositoryImpl implements ArmorTypeFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ArmorTypePanacheRepository panacheRepository;
    private final ArmorTypeMapper mapper;

    @Inject
    public ArmorTypeFindByIdRepositoryImpl(ArmorTypePanacheRepository panacheRepository, ArmorTypeMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ArmorType> findById(int id) {
        log.info(() -> "Finding armor type with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

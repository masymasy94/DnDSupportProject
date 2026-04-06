package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.WeaponTypeMapper;
import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.domain.repository.WeaponTypeFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class WeaponTypeFindByIdRepositoryImpl implements WeaponTypeFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final WeaponTypePanacheRepository panacheRepository;
    private final WeaponTypeMapper mapper;

    @Inject
    public WeaponTypeFindByIdRepositoryImpl(WeaponTypePanacheRepository panacheRepository, WeaponTypeMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<WeaponType> findById(int id) {
        log.info(() -> "Finding weapon type with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

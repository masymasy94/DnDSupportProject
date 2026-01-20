package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.WeaponTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.WeaponTypeMapper;
import com.dndplatform.compendium.domain.model.WeaponType;
import com.dndplatform.compendium.domain.repository.WeaponTypeFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class WeaponTypeFindByIdRepositoryImpl implements WeaponTypeFindByIdRepository, PanacheRepository<WeaponTypeEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final WeaponTypeMapper mapper;

    @Inject
    public WeaponTypeFindByIdRepositoryImpl(WeaponTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<WeaponType> findById(int id) {
        log.info(() -> "Finding weapon type with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

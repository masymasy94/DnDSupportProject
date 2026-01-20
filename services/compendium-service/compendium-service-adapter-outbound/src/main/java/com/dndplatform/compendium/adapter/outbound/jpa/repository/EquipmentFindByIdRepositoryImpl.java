package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.EquipmentEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.EquipmentMapper;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.repository.EquipmentFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class EquipmentFindByIdRepositoryImpl implements EquipmentFindByIdRepository, PanacheRepository<EquipmentEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EquipmentMapper mapper;

    @Inject
    public EquipmentFindByIdRepositoryImpl(EquipmentMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Equipment> findById(int id) {
        log.info(() -> "Finding Equipment with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.EquipmentMapper;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.repository.EquipmentFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class EquipmentFindByIdRepositoryImpl implements EquipmentFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EquipmentPanacheRepository panacheRepository;
    private final EquipmentMapper mapper;

    @Inject
    public EquipmentFindByIdRepositoryImpl(EquipmentPanacheRepository panacheRepository, EquipmentMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Equipment> findById(int id) {
        log.info(() -> "Finding Equipment with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

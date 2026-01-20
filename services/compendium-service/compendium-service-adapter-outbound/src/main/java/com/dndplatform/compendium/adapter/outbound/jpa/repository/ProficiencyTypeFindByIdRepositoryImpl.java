package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ProficiencyTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ProficiencyTypeMapper;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ProficiencyTypeFindByIdRepositoryImpl implements ProficiencyTypeFindByIdRepository, PanacheRepository<ProficiencyTypeEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ProficiencyTypeMapper mapper;

    @Inject
    public ProficiencyTypeFindByIdRepositoryImpl(ProficiencyTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<ProficiencyType> findById(int id) {
        log.info(() -> "Finding proficiency type with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

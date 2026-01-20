package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpellSchoolEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpellSchoolMapper;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.domain.repository.SpellSchoolFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class SpellSchoolFindByIdRepositoryImpl implements SpellSchoolFindByIdRepository, PanacheRepository<SpellSchoolEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SpellSchoolMapper mapper;

    @Inject
    public SpellSchoolFindByIdRepositoryImpl(SpellSchoolMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<SpellSchool> findById(int id) {
        log.info(() -> "Finding spell school with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

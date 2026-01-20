package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpellSchoolEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpellSchoolMapper;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.domain.repository.SpellSchoolFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class SpellSchoolFindAllRepositoryImpl implements SpellSchoolFindAllRepository, PanacheRepository<SpellSchoolEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SpellSchoolMapper mapper;

    @Inject
    public SpellSchoolFindAllRepositoryImpl(SpellSchoolMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<SpellSchool> findAllSpellSchools() {
        log.info(() -> "Finding all spell schools");

        return findAll(Sort.by("name")).list().stream()
                .map(mapper)
                .toList();
    }
}

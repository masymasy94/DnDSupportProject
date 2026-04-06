package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpellSchoolMapper;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.domain.repository.SpellSchoolFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class SpellSchoolFindAllRepositoryImpl implements SpellSchoolFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SpellSchoolPanacheRepository panacheRepository;
    private final SpellSchoolMapper mapper;

    @Inject
    public SpellSchoolFindAllRepositoryImpl(SpellSchoolPanacheRepository panacheRepository, SpellSchoolMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<SpellSchool> findAllSpellSchools() {
        log.info(() -> "Finding all spell schools");

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}

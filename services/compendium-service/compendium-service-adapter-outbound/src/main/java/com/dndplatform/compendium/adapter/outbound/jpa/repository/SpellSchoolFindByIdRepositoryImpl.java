package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpellSchoolMapper;
import com.dndplatform.compendium.domain.model.SpellSchool;
import com.dndplatform.compendium.domain.repository.SpellSchoolFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class SpellSchoolFindByIdRepositoryImpl implements SpellSchoolFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SpellSchoolPanacheRepository panacheRepository;
    private final SpellSchoolMapper mapper;

    @Inject
    public SpellSchoolFindByIdRepositoryImpl(SpellSchoolPanacheRepository panacheRepository, SpellSchoolMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<SpellSchool> findById(int id) {
        log.info(() -> "Finding spell school with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

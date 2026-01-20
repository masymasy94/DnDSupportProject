package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ProficiencyTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ProficiencyTypeMapper;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ProficiencyTypeFindAllRepositoryImpl implements ProficiencyTypeFindAllRepository, PanacheRepository<ProficiencyTypeEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ProficiencyTypeMapper mapper;

    @Inject
    public ProficiencyTypeFindAllRepositoryImpl(ProficiencyTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<ProficiencyType> findAllProficiencyTypes() {
        log.info(() -> "Finding all proficiency types");

        return findAll(Sort.by("name")).list().stream()
                .map(mapper)
                .toList();
    }
}

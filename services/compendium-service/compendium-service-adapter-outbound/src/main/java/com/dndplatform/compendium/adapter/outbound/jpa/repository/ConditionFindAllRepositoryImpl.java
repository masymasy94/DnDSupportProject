package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ConditionEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ConditionMapper;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.domain.repository.ConditionFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ConditionFindAllRepositoryImpl implements ConditionFindAllRepository, PanacheRepository<ConditionEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConditionMapper mapper;

    @Inject
    public ConditionFindAllRepositoryImpl(ConditionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Condition> findAllConditions() {
        log.info(() -> "Finding all conditions");

        return findAll(Sort.by("name")).list().stream()
                .map(mapper)
                .toList();
    }
}

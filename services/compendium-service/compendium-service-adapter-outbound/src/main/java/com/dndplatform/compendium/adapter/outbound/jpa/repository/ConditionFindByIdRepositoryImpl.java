package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ConditionEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ConditionMapper;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.domain.repository.ConditionFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ConditionFindByIdRepositoryImpl implements ConditionFindByIdRepository, PanacheRepository<ConditionEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConditionMapper mapper;

    @Inject
    public ConditionFindByIdRepositoryImpl(ConditionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Condition> findById(int id) {
        log.info(() -> "Finding condition with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

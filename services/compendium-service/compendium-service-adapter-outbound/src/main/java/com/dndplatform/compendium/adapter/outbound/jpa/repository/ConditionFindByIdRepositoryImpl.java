package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ConditionMapper;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.domain.repository.ConditionFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ConditionFindByIdRepositoryImpl implements ConditionFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConditionPanacheRepository panacheRepository;
    private final ConditionMapper mapper;

    @Inject
    public ConditionFindByIdRepositoryImpl(ConditionPanacheRepository panacheRepository, ConditionMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Condition> findById(int id) {
        log.info(() -> "Finding condition with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

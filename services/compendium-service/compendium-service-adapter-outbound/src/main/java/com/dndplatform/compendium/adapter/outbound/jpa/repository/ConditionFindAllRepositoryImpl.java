package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ConditionMapper;
import com.dndplatform.compendium.domain.model.Condition;
import com.dndplatform.compendium.domain.repository.ConditionFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ConditionFindAllRepositoryImpl implements ConditionFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ConditionPanacheRepository panacheRepository;
    private final ConditionMapper mapper;

    @Inject
    public ConditionFindAllRepositoryImpl(ConditionPanacheRepository panacheRepository, ConditionMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Condition> findAllConditions() {
        log.info(() -> "Finding all conditions");

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}

package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.filter.QueryFilterUtils;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.EquipmentMapper;
import com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.EquipmentFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class EquipmentFindAllRepositoryImpl implements EquipmentFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EquipmentPanacheRepository panacheRepository;
    private final EquipmentMapper mapper;

    @Inject
    public EquipmentFindAllRepositoryImpl(EquipmentPanacheRepository panacheRepository, EquipmentMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public PagedResult<Equipment> findAllEquipment(EquipmentFilterCriteria criteria) {
        log.info(() -> "Finding equipment with criteria: page=%d, pageSize=%d".formatted(criteria.page(), criteria.pageSize()));
        var filter = QueryFilterUtils.create(criteria);
        long totalElements = panacheRepository.countFiltered(filter.query(), filter.params());
        var content = panacheRepository.findFiltered(filter.query(), filter.params(), Sort.by("name"), criteria.page(), criteria.pageSize())
                .stream()
                .map(mapper)
                .toList();
        return new PagedResult<>(content, criteria.page(), criteria.pageSize(), totalElements);
    }
}

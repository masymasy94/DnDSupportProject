package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.filter.QueryFilterUtils;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.EquipmentEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.EquipmentMapper;
import com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.EquipmentFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class EquipmentFindAllRepositoryImpl implements EquipmentFindAllRepository, PanacheRepository<EquipmentEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final EquipmentMapper mapper;

    @Inject
    public EquipmentFindAllRepositoryImpl(EquipmentMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PagedResult<Equipment> findAllEquipment(EquipmentFilterCriteria criteria) {
        log.info(() -> "Finding equipment with criteria: page=%d, pageSize=%d".formatted(criteria.page(), criteria.pageSize()));

        var filter = QueryFilterUtils.create(criteria);
        var panacheQuery = find(filter.query(), Sort.by("name"), filter.params());
        long totalElements = panacheQuery.count();

        var content = panacheQuery
                .page(criteria.page(), criteria.pageSize())
                .list()
                .stream()
                .map(mapper)
                .toList();

        return getEquipmentPagedResult(criteria, content, totalElements);
    }

    private static @NonNull PagedResult<Equipment> getEquipmentPagedResult(EquipmentFilterCriteria criteria, List<Equipment> content, long totalElements) {
        return new PagedResult<>(
                content,
                criteria.page(),
                criteria.pageSize(),
                totalElements
        );
    }
}

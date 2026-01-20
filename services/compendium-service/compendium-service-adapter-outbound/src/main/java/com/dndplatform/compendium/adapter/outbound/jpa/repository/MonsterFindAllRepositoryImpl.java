package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.filter.QueryFilterUtils;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.MonsterEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.MonsterMapper;
import com.dndplatform.compendium.domain.filter.MonsterFilterCriteria;
import com.dndplatform.compendium.domain.model.Monster;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.MonsterFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class MonsterFindAllRepositoryImpl implements MonsterFindAllRepository, PanacheRepository<MonsterEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MonsterMapper mapper;

    @Inject
    public MonsterFindAllRepositoryImpl(MonsterMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PagedResult<Monster> findAllMonsters(MonsterFilterCriteria criteria) {
        log.info(() -> "Finding monsters with criteria: page=%d, pageSize=%d".formatted(criteria.page(), criteria.pageSize()));

        var filter = QueryFilterUtils.create(criteria);
        var panacheQuery = find(filter.query(), Sort.by("name"), filter.params());
        long totalElements = panacheQuery.count();

        var content = panacheQuery
                .page(criteria.page(), criteria.pageSize())
                .list()
                .stream()
                .map(mapper)
                .toList();

        return getMonsterPagedResult(criteria, content, totalElements);
    }




    private static @NonNull PagedResult<Monster> getMonsterPagedResult(MonsterFilterCriteria criteria, List<Monster> content, long totalElements) {
        return new PagedResult<>(
                content,
                criteria.page(),
                criteria.pageSize(),
                totalElements
        );
    }
}

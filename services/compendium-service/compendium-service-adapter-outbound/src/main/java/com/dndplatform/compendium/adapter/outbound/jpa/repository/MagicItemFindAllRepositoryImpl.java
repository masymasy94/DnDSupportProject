package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.filter.QueryFilterUtils;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.MagicItemEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.MagicItemMapper;
import com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.MagicItemFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class MagicItemFindAllRepositoryImpl implements MagicItemFindAllRepository, PanacheRepository<MagicItemEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MagicItemMapper mapper;

    @Inject
    public MagicItemFindAllRepositoryImpl(MagicItemMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PagedResult<MagicItem> findAllMagicItems(MagicItemFilterCriteria criteria) {
        log.info(() -> "Finding magic items with criteria: page=%d, pageSize=%d".formatted(criteria.page(), criteria.pageSize()));

        var filter = QueryFilterUtils.create(criteria);
        var panacheQuery = find(filter.query(), Sort.by("name"), filter.params());
        long totalElements = panacheQuery.count();

        var content = panacheQuery
                .page(criteria.page(), criteria.pageSize())
                .list()
                .stream()
                .map(mapper)
                .toList();

        return getMagicItemPagedResult(criteria, content, totalElements);
    }

    private static @NonNull PagedResult<MagicItem> getMagicItemPagedResult(MagicItemFilterCriteria criteria, List<MagicItem> content, long totalElements) {
        return new PagedResult<>(
                content,
                criteria.page(),
                criteria.pageSize(),
                totalElements
        );
    }
}

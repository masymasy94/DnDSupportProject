package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.filter.QueryFilterUtils;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpellMapper;
import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.domain.repository.SpellFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SpellFindAllRepositoryImpl implements SpellFindAllRepository {

    private final SpellPanacheRepository panacheRepository;
    private final SpellMapper mapper;

    @Inject
    public SpellFindAllRepositoryImpl(SpellPanacheRepository panacheRepository, SpellMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public PagedResult<Spell> findAllSpells(SpellFilterCriteria criteria) {
        var filter = QueryFilterUtils.create(criteria);
        long totalElements = panacheRepository.countFiltered(filter.query(), filter.params());
        var content = panacheRepository.findFiltered(filter.query(), filter.params(), Sort.by("level").and("name"), criteria.page(), criteria.pageSize())
                .stream().map(mapper).toList();
        return new PagedResult<>(content, criteria.page(), criteria.pageSize(), totalElements);
    }
}

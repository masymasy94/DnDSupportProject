package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.common.filter.QueryFilterUtils;
import com.dndplatform.compendium.adapter.outbound.jpa.entity.SpellEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpellMapper;
import com.dndplatform.compendium.domain.filter.SpellFilterCriteria;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.domain.repository.SpellFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class SpellFindAllRepositoryImpl implements SpellFindAllRepository, PanacheRepository<SpellEntity> {

    private final SpellMapper mapper;

    @Inject
    public SpellFindAllRepositoryImpl(SpellMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Spell> findAllSpells(SpellFilterCriteria criteria) {
        var filter = QueryFilterUtils.create(criteria);

        return find(filter.query(), Sort.by("level").and("name"), filter.params())
                .list().stream()
                .map(mapper)
                .toList();
    }
}

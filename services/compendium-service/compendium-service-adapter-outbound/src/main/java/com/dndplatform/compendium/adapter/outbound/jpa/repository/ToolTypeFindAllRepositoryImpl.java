package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ToolTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ToolTypeMapper;
import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.domain.repository.ToolTypeFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ToolTypeFindAllRepositoryImpl implements ToolTypeFindAllRepository, PanacheRepository<ToolTypeEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ToolTypeMapper mapper;

    @Inject
    public ToolTypeFindAllRepositoryImpl(ToolTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<ToolType> findAllToolTypes(String category) {
        log.info(() -> "Finding all tool types" + (category != null ? " with category: " + category : ""));

        if (category != null && !category.isBlank()) {
            return find("category", Sort.by("name"), category).list().stream()
                    .map(mapper)
                    .toList();
        }

        return findAll(Sort.by("name")).list().stream()
                .map(mapper)
                .toList();
    }
}

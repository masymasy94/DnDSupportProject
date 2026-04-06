package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ToolTypeMapper;
import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.domain.repository.ToolTypeFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ToolTypeFindAllRepositoryImpl implements ToolTypeFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ToolTypePanacheRepository panacheRepository;
    private final ToolTypeMapper mapper;

    @Inject
    public ToolTypeFindAllRepositoryImpl(ToolTypePanacheRepository panacheRepository, ToolTypeMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ToolType> findAllToolTypes(String category) {
        log.info(() -> "Finding all tool types" + (category != null ? " with category: " + category : ""));

        if (category != null && !category.isBlank()) {
            return panacheRepository.findByCategory(category).stream()
                    .map(mapper)
                    .toList();
        }

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}

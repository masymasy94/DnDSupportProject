package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.ToolTypeEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ToolTypeMapper;
import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.domain.repository.ToolTypeFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ToolTypeFindByIdRepositoryImpl implements ToolTypeFindByIdRepository, PanacheRepository<ToolTypeEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ToolTypeMapper mapper;

    @Inject
    public ToolTypeFindByIdRepositoryImpl(ToolTypeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<ToolType> findById(int id) {
        log.info(() -> "Finding tool type with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

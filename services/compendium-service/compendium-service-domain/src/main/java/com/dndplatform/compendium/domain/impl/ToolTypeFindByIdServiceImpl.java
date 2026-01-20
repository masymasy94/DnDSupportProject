package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.ToolTypeFindByIdService;
import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.domain.repository.ToolTypeFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ToolTypeFindByIdServiceImpl implements ToolTypeFindByIdService {

    private final ToolTypeFindByIdRepository repository;

    @Inject
    public ToolTypeFindByIdServiceImpl(ToolTypeFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public ToolType findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tool type not found with id: " + id));
    }
}

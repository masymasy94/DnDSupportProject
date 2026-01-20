package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.ToolTypeFindAllService;
import com.dndplatform.compendium.domain.model.ToolType;
import com.dndplatform.compendium.domain.repository.ToolTypeFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ToolTypeFindAllServiceImpl implements ToolTypeFindAllService {

    private final ToolTypeFindAllRepository repository;

    @Inject
    public ToolTypeFindAllServiceImpl(ToolTypeFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ToolType> findAll(String category) {
        return repository.findAllToolTypes(category);
    }
}

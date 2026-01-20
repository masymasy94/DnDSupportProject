package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.MagicItemFindByIdService;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.repository.MagicItemFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MagicItemFindByIdServiceImpl implements MagicItemFindByIdService {

    private final MagicItemFindByIdRepository repository;

    @Inject
    public MagicItemFindByIdServiceImpl(MagicItemFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public MagicItem findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Magic item not found with id: " + id));
    }
}

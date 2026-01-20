package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.MagicItemFindAllService;
import com.dndplatform.compendium.domain.filter.MagicItemFilterCriteria;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.domain.repository.MagicItemFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MagicItemFindAllServiceImpl implements MagicItemFindAllService {

    private final MagicItemFindAllRepository repository;

    @Inject
    public MagicItemFindAllServiceImpl(MagicItemFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public PagedResult<MagicItem> findAll(MagicItemFilterCriteria criteria) {
        return repository.findAllMagicItems(criteria);
    }
}

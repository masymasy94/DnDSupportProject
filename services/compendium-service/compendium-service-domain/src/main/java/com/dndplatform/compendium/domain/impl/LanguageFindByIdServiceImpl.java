package com.dndplatform.compendium.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.LanguageFindByIdService;
import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.domain.repository.LanguageFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LanguageFindByIdServiceImpl implements LanguageFindByIdService {

    private final LanguageFindByIdRepository repository;

    @Inject
    public LanguageFindByIdServiceImpl(LanguageFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public Language findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Language not found with id: " + id));
    }
}

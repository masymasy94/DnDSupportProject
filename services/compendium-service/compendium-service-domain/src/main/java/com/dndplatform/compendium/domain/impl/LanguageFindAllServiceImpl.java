package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.LanguageFindAllService;
import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.domain.repository.LanguageFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class LanguageFindAllServiceImpl implements LanguageFindAllService {

    private final LanguageFindAllRepository repository;

    @Inject
    public LanguageFindAllServiceImpl(LanguageFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Language> findAll() {
        return repository.findAllLanguages();
    }
}

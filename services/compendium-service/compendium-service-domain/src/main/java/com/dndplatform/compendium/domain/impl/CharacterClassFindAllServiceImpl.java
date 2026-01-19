package com.dndplatform.compendium.domain.impl;

import com.dndplatform.compendium.domain.CharacterClassFindAllService;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.domain.repository.CharacterClassFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CharacterClassFindAllServiceImpl implements CharacterClassFindAllService {

    private final CharacterClassFindAllRepository repository;

    @Inject
    public CharacterClassFindAllServiceImpl(CharacterClassFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CharacterClass> findAll() {
        return repository.findAllClasses();
    }
}

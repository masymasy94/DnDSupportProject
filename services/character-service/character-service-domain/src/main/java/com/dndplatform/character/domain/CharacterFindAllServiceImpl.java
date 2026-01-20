package com.dndplatform.character.domain;

import com.dndplatform.character.domain.model.PagedResult;
import com.dndplatform.character.domain.repository.CharacterFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class CharacterFindAllServiceImpl implements CharacterFindAllService {

    private final CharacterFindAllRepository repository;

    @Inject
    public CharacterFindAllServiceImpl(CharacterFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public PagedResult findAll(int page, int size) {
        return repository.findAll(page, size);
    }
}

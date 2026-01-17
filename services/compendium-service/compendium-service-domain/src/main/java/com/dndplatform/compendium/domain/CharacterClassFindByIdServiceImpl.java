package com.dndplatform.compendium.domain;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.domain.repository.CharacterClassFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class CharacterClassFindByIdServiceImpl implements CharacterClassFindByIdService {

    private final CharacterClassFindByIdRepository repository;

    @Inject
    public CharacterClassFindByIdServiceImpl(CharacterClassFindByIdRepository repository) {
        this.repository = repository;
    }

    @Override
    public CharacterClass findById(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Character class not found with id: " + id));
    }
}

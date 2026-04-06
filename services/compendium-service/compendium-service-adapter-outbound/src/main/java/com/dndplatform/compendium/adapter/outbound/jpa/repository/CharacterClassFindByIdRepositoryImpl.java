package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.CharacterClassMapper;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.domain.repository.CharacterClassFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterClassFindByIdRepositoryImpl implements CharacterClassFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterClassPanacheRepository panacheRepository;
    private final CharacterClassMapper mapper;

    @Inject
    public CharacterClassFindByIdRepositoryImpl(CharacterClassPanacheRepository panacheRepository, CharacterClassMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CharacterClass> findById(int id) {
        log.info(() -> "Finding Character Class with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

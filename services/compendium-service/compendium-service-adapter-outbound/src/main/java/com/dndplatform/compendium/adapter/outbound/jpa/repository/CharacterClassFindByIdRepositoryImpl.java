package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.CharacterClassEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.CharacterClassMapper;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.domain.repository.CharacterClassFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterClassFindByIdRepositoryImpl implements CharacterClassFindByIdRepository, PanacheRepository<CharacterClassEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterClassMapper mapper;

    @Inject
    public CharacterClassFindByIdRepositoryImpl(CharacterClassMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<CharacterClass> findById(int id) {
        log.info(() -> "Finding Character Class with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

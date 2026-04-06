package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.CharacterClassMapper;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.domain.repository.CharacterClassFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterClassFindAllRepositoryImpl implements CharacterClassFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CharacterClassPanacheRepository panacheRepository;
    private final CharacterClassMapper mapper;

    @Inject
    public CharacterClassFindAllRepositoryImpl(CharacterClassPanacheRepository panacheRepository, CharacterClassMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CharacterClass> findAllClasses() {
        log.info(() -> "Finding all Character Classes");

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}

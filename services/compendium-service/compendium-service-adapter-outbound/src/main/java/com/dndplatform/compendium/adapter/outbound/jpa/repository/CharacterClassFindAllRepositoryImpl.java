package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.CharacterClassEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.CharacterClassMapper;
import com.dndplatform.compendium.domain.model.CharacterClass;
import com.dndplatform.compendium.domain.repository.CharacterClassFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CharacterClassFindAllRepositoryImpl implements CharacterClassFindAllRepository, PanacheRepository<CharacterClassEntity> {

    private final java.util.logging.Logger log = Logger.getLogger(getClass().getName());
    private final CharacterClassMapper mapper;

    @Inject
    public CharacterClassFindAllRepositoryImpl(CharacterClassMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<CharacterClass> findAllClasses() {
        log.info(() -> "Finding all Character Classes");

        return findAll(Sort.by("name")).list().stream()
                .map(mapper)
                .toList();
    }
}

package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.LanguageEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.LanguageMapper;
import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.domain.repository.LanguageFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class LanguageFindByIdRepositoryImpl implements LanguageFindByIdRepository, PanacheRepository<LanguageEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final LanguageMapper mapper;

    @Inject
    public LanguageFindByIdRepositoryImpl(LanguageMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Language> findById(int id) {
        log.info(() -> "Finding language with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

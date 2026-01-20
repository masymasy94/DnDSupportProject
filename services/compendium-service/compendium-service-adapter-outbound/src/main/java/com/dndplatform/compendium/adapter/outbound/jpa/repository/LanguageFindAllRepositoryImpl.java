package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.LanguageEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.LanguageMapper;
import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.domain.repository.LanguageFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class LanguageFindAllRepositoryImpl implements LanguageFindAllRepository, PanacheRepository<LanguageEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final LanguageMapper mapper;

    @Inject
    public LanguageFindAllRepositoryImpl(LanguageMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Language> findAllLanguages() {
        log.info(() -> "Finding all languages");

        return findAll(Sort.by("name")).list().stream()
                .map(mapper)
                .toList();
    }
}

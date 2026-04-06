package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.LanguageMapper;
import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.domain.repository.LanguageFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class LanguageFindAllRepositoryImpl implements LanguageFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final LanguagePanacheRepository panacheRepository;
    private final LanguageMapper mapper;

    @Inject
    public LanguageFindAllRepositoryImpl(LanguagePanacheRepository panacheRepository, LanguageMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Language> findAllLanguages() {
        log.info(() -> "Finding all languages");

        return panacheRepository.listAll(Sort.by("name")).stream()
                .map(mapper)
                .toList();
    }
}

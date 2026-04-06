package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.LanguageMapper;
import com.dndplatform.compendium.domain.model.Language;
import com.dndplatform.compendium.domain.repository.LanguageFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class LanguageFindByIdRepositoryImpl implements LanguageFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final LanguagePanacheRepository panacheRepository;
    private final LanguageMapper mapper;

    @Inject
    public LanguageFindByIdRepositoryImpl(LanguagePanacheRepository panacheRepository, LanguageMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Language> findById(int id) {
        log.info(() -> "Finding language with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

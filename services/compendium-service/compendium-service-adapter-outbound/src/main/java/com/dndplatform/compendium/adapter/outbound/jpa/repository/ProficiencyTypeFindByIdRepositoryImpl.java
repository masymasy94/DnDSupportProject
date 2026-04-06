package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.ProficiencyTypeMapper;
import com.dndplatform.compendium.domain.model.ProficiencyType;
import com.dndplatform.compendium.domain.repository.ProficiencyTypeFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class ProficiencyTypeFindByIdRepositoryImpl implements ProficiencyTypeFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final ProficiencyTypePanacheRepository panacheRepository;
    private final ProficiencyTypeMapper mapper;

    @Inject
    public ProficiencyTypeFindByIdRepositoryImpl(ProficiencyTypePanacheRepository panacheRepository, ProficiencyTypeMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ProficiencyType> findById(int id) {
        log.info(() -> "Finding proficiency type with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

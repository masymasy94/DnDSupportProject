package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.FeatMapper;
import com.dndplatform.compendium.domain.model.Feat;
import com.dndplatform.compendium.domain.repository.FeatFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class FeatFindByIdRepositoryImpl implements FeatFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final FeatPanacheRepository panacheRepository;
    private final FeatMapper mapper;

    @Inject
    public FeatFindByIdRepositoryImpl(FeatPanacheRepository panacheRepository, FeatMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Feat> findById(int id) {
        log.info(() -> "Finding Feat with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

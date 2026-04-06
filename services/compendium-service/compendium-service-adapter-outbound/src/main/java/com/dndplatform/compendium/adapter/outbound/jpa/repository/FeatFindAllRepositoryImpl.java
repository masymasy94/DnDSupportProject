package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.FeatMapper;
import com.dndplatform.compendium.domain.model.Feat;
import com.dndplatform.compendium.domain.repository.FeatFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class FeatFindAllRepositoryImpl implements FeatFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final FeatPanacheRepository panacheRepository;
    private final FeatMapper mapper;

    @Inject
    public FeatFindAllRepositoryImpl(FeatPanacheRepository panacheRepository, FeatMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Feat> findAllFeats() {
        log.info(() -> "Finding all feats");
        return panacheRepository.listAll(Sort.by("name")).stream().map(mapper).toList();
    }
}

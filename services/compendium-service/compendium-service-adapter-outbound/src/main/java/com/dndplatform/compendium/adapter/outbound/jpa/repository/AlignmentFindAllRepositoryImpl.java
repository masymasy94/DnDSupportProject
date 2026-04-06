package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.AlignmentMapper;
import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.domain.repository.AlignmentFindAllRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class AlignmentFindAllRepositoryImpl implements AlignmentFindAllRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final AlignmentPanacheRepository panacheRepository;
    private final AlignmentMapper mapper;

    @Inject
    public AlignmentFindAllRepositoryImpl(AlignmentPanacheRepository panacheRepository, AlignmentMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Alignment> findAllAlignment() {
        log.info(() -> "Finding all alignments");

        return panacheRepository.listAll(Sort.by("id")).stream()
                .map(mapper)
                .toList();
    }
}

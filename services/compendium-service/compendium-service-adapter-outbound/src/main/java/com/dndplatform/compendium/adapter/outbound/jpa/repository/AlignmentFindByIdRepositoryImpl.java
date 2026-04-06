package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.AlignmentMapper;
import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.domain.repository.AlignmentFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class AlignmentFindByIdRepositoryImpl implements AlignmentFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final AlignmentPanacheRepository panacheRepository;
    private final AlignmentMapper mapper;

    @Inject
    public AlignmentFindByIdRepositoryImpl(AlignmentPanacheRepository panacheRepository, AlignmentMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Alignment> findById(int id) {
        log.info(() -> "Finding Alignment with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

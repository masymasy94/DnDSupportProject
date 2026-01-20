package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.AlignmentEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.AlignmentMapper;
import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.domain.repository.AlignmentFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class AlignmentFindByIdRepositoryImpl implements AlignmentFindByIdRepository, PanacheRepository<AlignmentEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final AlignmentMapper mapper;

    @Inject
    public AlignmentFindByIdRepositoryImpl(AlignmentMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Alignment> findById(int id) {
        log.info(() -> "Finding Alignment with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.AlignmentEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.AlignmentMapper;
import com.dndplatform.compendium.domain.model.Alignment;
import com.dndplatform.compendium.domain.repository.AlignmentFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class AlignmentFindAllRepositoryImpl implements AlignmentFindAllRepository, PanacheRepository<AlignmentEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final AlignmentMapper mapper;

    @Inject
    public AlignmentFindAllRepositoryImpl(AlignmentMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Alignment> findAllAlignment() {
        log.info(() -> "Finding all alignments");

        return findAll(Sort.by("id")).list().stream()
                .map(mapper)
                .toList();
    }
}

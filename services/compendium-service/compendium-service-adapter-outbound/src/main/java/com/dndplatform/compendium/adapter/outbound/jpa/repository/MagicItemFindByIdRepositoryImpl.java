package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.MagicItemEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.MagicItemMapper;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.repository.MagicItemFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class MagicItemFindByIdRepositoryImpl implements MagicItemFindByIdRepository, PanacheRepository<MagicItemEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MagicItemMapper mapper;

    @Inject
    public MagicItemFindByIdRepositoryImpl(MagicItemMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<MagicItem> findById(int id) {
        log.info(() -> "Finding Magic Item with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

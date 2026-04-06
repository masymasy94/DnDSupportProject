package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.MagicItemMapper;
import com.dndplatform.compendium.domain.model.MagicItem;
import com.dndplatform.compendium.domain.repository.MagicItemFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class MagicItemFindByIdRepositoryImpl implements MagicItemFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final MagicItemPanacheRepository panacheRepository;
    private final MagicItemMapper mapper;

    @Inject
    public MagicItemFindByIdRepositoryImpl(MagicItemPanacheRepository panacheRepository, MagicItemMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<MagicItem> findById(int id) {
        log.info(() -> "Finding Magic Item with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

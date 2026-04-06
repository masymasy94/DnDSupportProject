package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SpellMapper;
import com.dndplatform.compendium.domain.model.Spell;
import com.dndplatform.compendium.domain.repository.SpellFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class SpellFindByIdRepositoryImpl implements SpellFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SpellPanacheRepository panacheRepository;
    private final SpellMapper mapper;

    @Inject
    public SpellFindByIdRepositoryImpl(SpellPanacheRepository panacheRepository, SpellMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Spell> findById(int id) {
        log.info(() -> "Finding Spell with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

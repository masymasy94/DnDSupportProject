package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SkillMapper;
import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.domain.repository.SkillFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class SkillFindByIdRepositoryImpl implements SkillFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SkillPanacheRepository panacheRepository;
    private final SkillMapper mapper;

    @Inject
    public SkillFindByIdRepositoryImpl(SkillPanacheRepository panacheRepository, SkillMapper mapper) {
        this.panacheRepository = panacheRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Skill> findById(int id) {
        log.info(() -> "Finding skill with id: " + id);

        return panacheRepository.findByIdOptional((long) id)
                .map(mapper);
    }
}

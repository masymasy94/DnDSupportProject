package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.SkillEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SkillMapper;
import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.domain.repository.SkillFindByIdRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class SkillFindByIdRepositoryImpl implements SkillFindByIdRepository, PanacheRepository<SkillEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SkillMapper mapper;

    @Inject
    public SkillFindByIdRepositoryImpl(SkillMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<Skill> findById(int id) {
        log.info(() -> "Finding skill with id: " + id);

        return findByIdOptional((long) id)
                .map(mapper);
    }
}

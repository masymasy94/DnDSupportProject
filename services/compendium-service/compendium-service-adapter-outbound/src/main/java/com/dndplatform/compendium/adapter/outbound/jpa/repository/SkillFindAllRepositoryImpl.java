package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.SkillEntity;
import com.dndplatform.compendium.adapter.outbound.jpa.mapper.SkillMapper;
import com.dndplatform.compendium.domain.model.Skill;
import com.dndplatform.compendium.domain.repository.SkillFindAllRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class SkillFindAllRepositoryImpl implements SkillFindAllRepository, PanacheRepository<SkillEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SkillMapper mapper;

    @Inject
    public SkillFindAllRepositoryImpl(SkillMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Skill> findAllSkills() {
        log.info(() -> "Finding all skills");

        return findAll(Sort.by("name")).list().stream()
                .map(mapper)
                .toList();
    }
}
